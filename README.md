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

See this [**example**](https://github.com/ascatox/hlf-android-client/blob/master/MyApplication/app/build.gradle)

### .jar
Copy inside the folder  `\MyApplication\app\libs`  these [libraries](https://github.com/ascatox/hlf-android-client/tree/master/MyApplication/app/libs)


### Crypto-config

Download the folder `crypto-users` from your HyperLedger Fabric installation. <br>

Copy your dir `crypto-users` and `config-fabric-network.json` into the device's folder `Download` </br>

You can find an example of `config-fabric-network.json` --> [here](https://github.com/ascatox/hlf-android-client/blob/master/config-fabric-network.json)


### API

**See** this [link](https://github.com/ascatox/hlf-android-client/blob/master/hlf-android-client/src/main/java/it/eng/hlf/android/client/CustodyLedgerClient.java) for API's list.

### Usage
```
InputStream configFabric = getAssets().open("config-fabric-network.json");
InputStream cert = getAssets().open("ca-cert.pem");
InputStream keystore = getAssets().open("keystore");
final FabricCustodyLedgerClient fabricCustodyLedgerClient = new FabricCustodyLedgerClient(configFabric, cert, keystore);
 
```
 
