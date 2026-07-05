-- ===========================
-- INPUT
-- ===========================

local key = KEYS[1]

local capacity = tonumber(ARGV[1])
local refillRate = tonumber(ARGV[2])
local currentTime = tonumber(ARGV[3])

-- ===========================
-- READ BUCKET
-- ===========================

local tokens = redis.call('HGET', key, 'tokens')
local lastRefillTime = redis.call('HGET', key, 'lastRefillTime')

-- ===========================
-- FIRST REQUEST
-- ===========================

if not tokens then
    tokens = capacity
    lastRefillTime = currentTime
else
    tokens = tonumber(tokens)
    lastRefillTime = tonumber(lastRefillTime)
end

-- ===========================
-- REFILL TOKENS
-- ===========================

local elapsedTime = (currentTime - lastRefillTime) / 1000

local refillTokens = elapsedTime * refillRate

tokens = math.min(
    capacity,
    tokens + refillTokens
)

lastRefillTime = currentTime

-- ===========================
-- CONSUME TOKEN
-- ===========================

if tokens >= 1 then

    tokens = tokens - 1

    redis.call('HSET',
        key,
        'tokens',
        tokens,
        'lastRefillTime',
        lastRefillTime
    )

    return 1

end

-- ===========================
-- SAVE STATE
-- ===========================

redis.call('HSET',
    key,
    'tokens',
    tokens,
    'lastRefillTime',
    lastRefillTime
)

return 0