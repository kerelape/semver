# Semver ![elegantobjects](https://www.elegantobjects.org/badge.svg)

Object-oriented [Semantic Versioning](https://semver.org) implementation.

## How to use

Add dependency to your project:
```xml
<dependency>
  <groupId>io.github.kerelape</groupId>
  <artifactId>semver</artifactId>
  <version>0.1.0</version>
</dependency>
```
> [!Note]
> Currently the project is only distributed on GitHub packages,
> so, to use it - read [this tutorial](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)

Create a new `Version` object:
```kotlin
val version = Version("1.2.3-alpha.4+567890")
```

Get segments of the version:
```kotlin
val major: Number = version.Major() // 1
val minor: Number = version.Minor() // 2
val patch: Number = version.Patch() // 3
val prerelease: Text = version.PreRelease() // alpha.4
val build: Text = version.Build() // 567890
```

## How to contribute
Fork the repository, make some changes, and open a new pull request.

> [!Note]
> Before you send us the changes, confirm they are nicely formatted by
> running `mvn clean install -Pktlint`
