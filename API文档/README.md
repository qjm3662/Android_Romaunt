#hackday
git 代码地址：https://git.oschina.net/mrbian/hackday.git
##服务器和返回信息

url:139.129.42.180

port:3000

返回格式说明：

  成功返回status:true,请求失败返回status:false，

  错误信息在msg里面
```
{
  "status":"true",
  "msg":{}
}

----------------
{
  "status":"false",
  "msg":"wrong msg content"
}
```

##登录注册
###登录 @method post
param  手机号 mobile

param  密码 password

return
```
{
    "status":"true",
    "msg":
      {
        "userID": "INT",
        "LoginToken": "STRING",
        "token" : "STRING"
      }
}
```

###注册  @method post


param 手机号 mobile

param 密码 password

param 用户昵称 userName

return

```
{
    "status":"true",
   " msg":
      {
        "userID": "INT",
        "LoginToken": "STRING",
        "token" : "STRING"
      }
}
```

###LoginToken失效后得到新的token  @method post
param token

return
```
{
    "status":"true",
   " msg":
      {
        "userID": "INT",
        "LoginToken": "STRING",
        "token" : "STRING"
      }
}
```