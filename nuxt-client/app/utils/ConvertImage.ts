import { OperationCanceledException } from "typescript";

export default function compressAndConvertImage(
  file: File,
  outputMimeType: string,
  quality: number,
) {
  return new Promise<string>((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = function (event) {
      const img = new Image();
      img.onload = function () {
        const canvas = document.createElement("canvas");
        canvas.width = img.naturalWidth;
        canvas.height = img.naturalHeight;
        const ctx = canvas.getContext("2d");
        if (!ctx) {
          throw new OperationCanceledException();
        }
        ctx.drawImage(img, 0, 0);

        // Convert and compress
        const compressedDataUrl = canvas.toDataURL(outputMimeType, quality);
        resolve(compressedDataUrl);
      };
      img.onerror = reject;
      img.src = event.target!.result as string;
    };
    reader.onerror = reject;
    reader.readAsDataURL(file);
  });
}
