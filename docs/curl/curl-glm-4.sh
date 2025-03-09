curl -X POST \
        -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsInNpZ25fdHlwZSI6IlNJR04ifQ.eyJhcGlfa2V5IjoiNDc4YWRjZmZkM2ZmNDcxZGI0OGRjYjk1ZTc2NjQ1NzIiLCJleHAiOjE3NDE1MzM1NTg2NDIsInRpbWVzdGFtcCI6MTc0MTUzMTc1ODY0OH0.xko1rwDgkOTbbYXdQQXH30PKsCnz1Dd7ys1XqgbvQs0" \
        -H "Content-Type: application/json" \
        -H "User-Agent: Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)" \
        -d '{
          "model":"glm-4",
          "stream": "true",
          "messages": [
              {
                  "role": "user",
                  "content": "1+1"
              }
          ]
        }' \
  https://open.bigmodel.cn/api/paas/v4/chat/completions
