# 上传说明

## 配置

`gradle.properties`设置`bintray.user`和`bintray.apikey`参数。

```
bintray.user=kkmike999
bintray.apikey=******
```

在[bintry profile edit](https://bintray.com/profile/edit)找到`api key`:

![](https://github.com/linglongxin24/SQLite/blob/master/screenshorts/jcenter_edit_profile.png?raw=true)

![](https://github.com/linglongxin24/SQLite/blob/master/screenshorts/jcenter_get_api_key.png?raw=true)

## 命令

```
gradle :YuiHatano:bintrayUpload
```

建议设置代理，bintray可能被墙。参考[Gradle 设置代理](http://chaosleong.github.io/2017/02/10/Configuring-Gradle-Proxy/)

```
gradle :YuiHatano:bintrayUpload -DsocksProxyHost=127.0.0.1 -DsocksProxyPort=1086
```

> - -D 代表添加参数
> - socksProxyHost 代理ip或域名
> - socksProxyPort 代理端口

