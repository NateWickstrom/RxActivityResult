RxActivityResult
================
[ ![Download](https://api.bintray.com/packages/natewickstrom/natewickstrom/RxActivityResult/images/download.svg) ](https://bintray.com/natewickstrom/natewickstrom/RxActivityResult/_latestVersion)
An Rx wrapper for receiving results from `startActivityForResult()` as an Observable.

Download
--------
To launch from an Activity :
```groovy
compile 'com.natewickstrom.rxactivityresult:rxactivityresult:0.1.0-rc1'
```
To launch from a Fragment :
```groovy
compile 'com.natewickstrom.rxactivityresult:rxactivityresult-support-v4:0.1.0-rc1'
```

Usage
--------
Example (with Lambdas for brevity):

```java
static final int REQUEST_IMAGE_CAPTURE = 1;
 
RxPermissions.getInstance(this).from(this)
    .startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAPTURE)
    .subscribe(result -> {
        if (result.getRequestCode() == REQUEST_IMAGE_CAPTURE && result.isOk() {
            Bundle extras = result.getData().getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    });
```

For a slightly more in depth example see the Sample Project.

License
-------

    Copyright (C) 2016 Nathan Wickstrom

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
