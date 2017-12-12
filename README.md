![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=for-the-badge)
# KMS-Android-Sample
This is the sample project created to demonstrate Key Management capabilities of the Alibaba Cloud

## Built using

 - Android Studio 3.0.0
 - KMS SDK 2.4.0
 - Core SDK 3.2.8

## Overview of this application

This application shows a demo of "Key Management Service" capability of the Alibaba Cloud. Please find the device screenshots in "snapshots" folder.

## Prerequisites

 - You need a Alibaba Cloud Account. If you want one you can get one with **free credit of $300** by registering [here](https://account-intl.aliyun.com/register/intl_register.htm?biz_params=%7B%22intl%22%3A%22%7B%5C%22referralCode%5C%22%3A%5C%226qnq3f%5C%22%7D%22%7D)

## Installation

 1. Clone or download the project into your Android Studio 3.0.0 
 2. You need the /libs folder for proper functioning. **DON'T REMOVE LIBS FOLDER.**

### Access Keys Information
Please replace your information from your Alibaba Cloud console in "strings.xml".

```
<resources>
    <string name="app_name">KMSSample</string>

    <!-- Alibaba KMS Service details-->
    <!-- Please replace this details with your own-->
    <!--Public Endpoint-->
    <string name="regionId">UPDATE YOUR REGION ID HERE Ex. cn-hangzhou</string>
    <!-- Access ID -->
    <string name="AccessKey">UPDATE YOUR ACCESS ID</string>
    <!-- Access key Secret -->
    <string name="AccessKeySecret">UPDATE YOUR ACCESS KEY HERE</string>

    <string name="FILE_NAME">LOGIN_DETAILS</string>
    <string name="CREDENTIALS">CREDENTIALS</string>
    <string name="FORM_DATA">FORM_DATA</string>

</resources>
```

## Bugs & Feedback
For bugs, questions and discussions please use the [Github Issues.](https://github.com/saichandu415/KMS-Android-Sample/issues)


## Snapshots



|                                          Home Screen                                       |                                               User Form                                              | 
|:----------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------:|
|<img src="https://github.com/saichandu415/KMS-Android-Sample/raw/master/snapshots/Home_Screen.png" width="432" height="768" /> |<img src="https://github.com/saichandu415/KMS-Android-Sample/raw/master/snapshots/After_Data.png" width="432" height="768" />|

|                                          Encrypted Credentials                                      |                                               Decrypted Credentials                                              | 
|:----------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------:|
|<img src="https://github.com/saichandu415/KMS-Android-Sample/raw/master/snapshots/Encrypt_Cred.png" width="432" height="768" /> |<img src="https://github.com/saichandu415/KMS-Android-Sample/raw/master/snapshots/Decrypt_Cred.png" width="432" height="768" />|

|                                          Encrypted User Values                                      |                                               Decrypted User Values                                              | 
|:----------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------:|
|<img src="https://github.com/saichandu415/KMS-Android-Sample/raw/master/snapshots/Form_Data.png" width="432" height="768" /> |<img src="https://github.com/saichandu415/KMS-Android-Sample/raw/master/snapshots/Decrypt_Form.png" width="432" height="768" />|

## License

    Copyright (c) 2017-present, Sarath Chandra.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
