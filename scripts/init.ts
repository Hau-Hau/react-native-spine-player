import { execSync } from "child_process";
import { promises } from "fs";
import decompress from "decompress";

const shopifyReactNativeSkiaVersion = "0.1.232";
const shopifyReactNativeSkiaDirectoryName = `shopify-react-native-skia-${shopifyReactNativeSkiaVersion.replaceAll(
  ".",
  "-",
)}`;
const spine2dCommitHash = "f906fb0733fa53dea2965285c88ff6d33d83f6ac";

const spinePromise = Promise.all([
  () => console.log("[get-spine-cpp] Preparing directories..."),
  promises.mkdir(`./externals/spine-runtimes/`, { recursive: true }),
  promises.mkdir(`./cpp/spine-cpp/`, { recursive: true }),
]).then(() => {
  console.log("[get-spine-cpp] Pulling EsotericSoftware/spine-runtimes repository...");
  execSync(`git clone https://github.com/EsotericSoftware/spine-runtimes.git ./externals/spine-runtimes`);
  execSync(`git checkout ${spine2dCommitHash}`, { cwd: "./externals/spine-runtimes" });
  console.log("[get-spine-cpp] Copying...");
  promises
    .cp(`./externals/spine-runtimes/spine-cpp/spine-cpp/`, "./cpp/spine-cpp/", { force: true, recursive: true })
    .then(() => {
      console.log("[get-spine-cpp] Cleaning...");
      promises.rm(`./externals/spine-runtimes`, { force: true, recursive: true }).then(() => {
        console.log("[get-spine-cpp] Done!");
      });
    });
});

const skiaPromise = promises.mkdir(`./externals/`, { recursive: true }).then(() => {
  console.log(`[get-skia] Fetching react-native-skia@${shopifyReactNativeSkiaVersion}`);
  execSync(`npm pack @shopify/react-native-skia@${shopifyReactNativeSkiaVersion} --pack-destination ./externals/`);
  decompress(
    `./externals/shopify-react-native-skia-${shopifyReactNativeSkiaVersion}.tgz`,
    `./externals/${shopifyReactNativeSkiaDirectoryName}`,
  ).then(() => {
    console.log("[get-skia] Preparing directories...");
    Promise.all([
      promises.mkdir(`./libs/android/arm64-v8a/`, { recursive: true }),
      promises.mkdir(`./libs/android/armeabi-v7a/`, { recursive: true }),
      promises.mkdir(`./libs/android/x86/`, { recursive: true }),
      promises.mkdir(`./libs/android/x86_64/`, { recursive: true }),
      promises.mkdir(`./libs/ios/libskia.xcframework/ios-arm64_arm64e/`, { recursive: true }),
      promises.mkdir(`./libs/ios/libskia.xcframework/ios-arm64_arm64e_x86_64-simulator/`, { recursive: true }),
      promises.mkdir(`./cpp/skia/`, { recursive: true }),
    ]).then(() => {
      console.log("[get-skia] Copying...");
      Promise.all([
        promises.copyFile(
          `./externals/${shopifyReactNativeSkiaDirectoryName}/package/libs/android/arm64-v8a/libskia.a`,
          "./libs/android/arm64-v8a/libskia.a",
        ),
        promises.copyFile(
          `./externals/${shopifyReactNativeSkiaDirectoryName}/package/libs/android/armeabi-v7a/libskia.a`,
          "./libs/android/armeabi-v7a/libskia.a",
        ),
        promises.copyFile(
          `./externals/${shopifyReactNativeSkiaDirectoryName}/package/libs/android/x86/libskia.a`,
          "./libs/android/x86/libskia.a",
        ),
        promises.copyFile(
          `./externals/${shopifyReactNativeSkiaDirectoryName}/package/libs/android/x86_64/libskia.a`,
          "./libs/android/x86_64/libskia.a",
        ),
        promises.copyFile(
          `./externals/${shopifyReactNativeSkiaDirectoryName}/package/libs/ios/libskia.xcframework/ios-arm64_arm64e/libskia.a`,
          "./libs/ios/libskia.xcframework/ios-arm64_arm64e/libskia.a",
        ),
        promises.copyFile(
          `./externals/${shopifyReactNativeSkiaDirectoryName}/package/libs/ios/libskia.xcframework/ios-arm64_arm64e_x86_64-simulator/libskia.a`,
          "./libs/ios/libskia.xcframework/ios-arm64_arm64e_x86_64-simulator/libskia.a",
        ),
        promises.copyFile(
          `./externals/${shopifyReactNativeSkiaDirectoryName}/package/libs/ios/libskia.xcframework/Info.plist`,
          "./libs/ios/libskia.xcframework/Info.plist",
        ),
        promises.cp(`./externals/${shopifyReactNativeSkiaDirectoryName}/package/cpp/skia`, "./cpp/skia/", {
          recursive: true,
        }),
      ]).then(() => {
        console.log("[get-skia] Cleaning...");
        Promise.all([
          promises.rm(`./externals/shopify-react-native-skia-${shopifyReactNativeSkiaVersion}.tgz`, { force: true }),
          promises.rm(`./externals/${shopifyReactNativeSkiaDirectoryName}`, { force: true, recursive: true }),
        ]).then(() => {
          console.log("[get-skia] Done!");
        });
      });
    });
  });
});

Promise.all([spinePromise, skiaPromise]);
