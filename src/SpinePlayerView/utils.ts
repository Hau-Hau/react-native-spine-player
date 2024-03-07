import { Image, ImageRequireSource, Platform } from "react-native";

const resolveDefaultSource = (defaultSource?: ImageRequireSource): string => {
  if (!defaultSource) {
    return "";
  }
  if (Platform.OS === "android") {
    // Android receives a URI string, and resolves into a Drawable using RN's methods.
    const resolved = Image.resolveAssetSource(defaultSource as ImageRequireSource);

    if (resolved) {
      return resolved.uri;
    }

    return "";
  }

  // TODO Handle iOS scenario later
  // iOS or other number mapped assets
  // In iOS the number is passed, and bridged automatically into a UIImage
  // return defaultSource;
  return "";
};

function isValidJson(item: any): boolean {
  let value = typeof item !== "string" ? JSON.stringify(item) : item;
  try {
    value = JSON.parse(value);
  } catch {
    return false;
  }

  return typeof value === "object" && value !== null;
}

export { resolveDefaultSource, isValidJson };
