
local key = KEYS[1]
local deductStock = tonumber(ARGV[1])
local current = redis.call('get', key)
if tonumber(current) > deductStock then
    redis.call('decrby', key, deductStock)
    return redis.call('get', key)
else
    return -1
end