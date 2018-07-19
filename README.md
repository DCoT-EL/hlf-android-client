# hlf-android-client

## Usage For APK

### build.grandle 

Add this dependencies: <br>

 ```
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation 'org.slf4j:slf4j-api:1.7.21'
    implementation 'org.slf4j:slf4j-android:1.7.21'
    implementation 'com.fasterxml.jackson.core:jackson-core:2.9.5'
    implementation 'com.fasterxml.jackson.core:jackson-annotations:2.9.5'
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.9.5'
```

see this **example** --> [link](https://github.com/ascatox/hlf-android-client/blob/master/MyApplication/app/build.gradle)

### .jar
Copy inside the folder  `\MyApplication\app\libs`  this library .jar --> [link](https://github.com/ascatox/hlf-android-client/tree/master/MyApplication/app/libs)


### Crypto-config

Download the folder crypto-config from your HyperLedger Fabric istallation. <br>

`cd MyApplication\data`  and copy the dir `crypto-config` and `config-fabric-network.json` into the device's folder `Download` </br>




