# Nautilus Plugin NRP

Nautilus plugin for the Next Release Problem (NRP)

[![GitHub Release](https://img.shields.io/github/release/nautilus-framework/nautilus-plugin-nrp.svg)](https://github.com/nautilus-framework/nautilus-plugin-nrp/releases/latest)
[![GitHub contributors](https://img.shields.io/github/contributors/nautilus-framework/nautilus-plugin-nrp.svg)](https://github.com/nautilus-framework/nautilus-plugin-nrp/graphs/contributors)
[![GitHub stars](https://img.shields.io/github/stars/nautilus-framework/nautilus-plugin-nrp.svg)](https://github.com/almende/nautilus-framework/nautilus-plugin-nrp)
[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)

## Requirements

You need to download Nautilus Framework in your machine (available at https://github.com/nautilus-framework/nautilus-framework) before installing this plugin

## Usage

Open the pom.xml file at ```nautilus-web``` module (from Nautilus Framework) and add the following maven dependency:

```xml
<dependency>
  <groupId>org.nautilus.plugin</groupId>
  <artifactId>nautilus-plugin-nrp</artifactId>
  <version>LATEST_VERSION</version>
</dependency>
```

At ```nautilus-web``` search for `PluginService` and add the following fragment code at ```loadPluginsFromClasspath``` method:

```java
addProblemExtension(new NRPProblemExtension());
```

Run Nautilus Framework as usual. If everything is working well, when you initiate a new execution, the NRP problem is going to be available as follows:

<kbd>
  <img src="https://user-images.githubusercontent.com/114015/100835879-5396b580-343c-11eb-8df4-c0933eddacc8.png">
</kbd>

## Objective Functions

This plugin supports the following objective functions

1. Cost
2. Importance
3. Number of Requirements
4. Number of Tasks
5. Profit

Please feel free to pull request a new set of objectives.

## Problem Instances

This plugins makes available the following list of requirements (problem instances) for your experiments:

1. **r025**: 25 requirements to be selected (randomly generated)
2. **r100**: 100 requirements to be selected (randomly generated)

## Screenshot

The following image shows an execution considering all objective functions

<kbd>
  <img src="https://user-images.githubusercontent.com/114015/100836227-ef282600-343c-11eb-863e-d06b7bd43a02.png">
</kbd>


## Questions or Suggestions

Feel free to create <a href="https://github.com/nautilus-framework/nautilus-plugin-nrp/issues">issues</a> here as you need

## Contribute

Contributions to the this project are very welcome! We can't do this alone! Feel free to fork this project, work on it and then make a pull request.

## Authors

* **Thiago Ferreira** - *Initial work*

See also the list of [contributors](https://github.com/nautilus-framework/nautilus-plugin-nrp/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
