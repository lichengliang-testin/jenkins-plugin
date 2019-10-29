# TestinPro Jenkins Plugin 

## 测试环境搭建

- JDK
- Maven

### ~/.m2/settings 配置文件

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <pluginGroups>
    <pluginGroup>org.jenkins-ci.tools</pluginGroup>
  </pluginGroups>
  <profiles>
    <profile>
      <id>jenkins</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <repositories>
        <repository>
          <id>repo.jenkins-ci.org</id>
          <url>http://repo.jenkins-ci.org/public/</url>
        </repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
          <id>repo.jenkins-ci.org</id>
          <url>http://repo.jenkins-ci.org/public/</url>
        </pluginRepository>
      </pluginRepositories>
    </profile>
  </profiles>
</settings>
```

## 运行插件（实际上是启动一个 Jenkins）

- 运行命令 `mvn hpi:run `
- 打开 `http://127.0.0.1:8080/jenkins`
- 按 `回车` 重新加载

## 生成安装包

`mvn package`

## 参考资料

- https://wiki.jenkins.io/display/JENKINS/Extend+Jenkins
- https://wiki.jenkins.io/display/JENKINS/Architecture
- https://wiki.jenkins.io/display/JENKINS/Plugin+Cookbook
- https://github.com/jenkinsci/dingding-notifications-plugin