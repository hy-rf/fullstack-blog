<script setup lang="ts">
interface Props {
  image: File;
}
const props = defineProps<Props>();

const imageRef = toRef(props, "image");

const canvasRef = ref<HTMLCanvasElement>();

const x = ref<number>(0);
const y = ref<number>(0);
const isDragging = ref<boolean>(false);

const offsetX = ref(0);
const offsetY = ref(0);

watch(imageRef, async () => {
  const bitmap = await createImageBitmap(imageRef.value, 0, 0, 300, 300);
  canvasRef.value?.getContext("2d")?.drawImage(bitmap, 0, 0);
});

function downloadImage() {
  const canvas = canvasRef.value;
  if (canvas) {
    const link = document.createElement("a");
    link.download = "cropped-image.png";
    link.href = canvas.toDataURL("image/png");
    link.click();
  }
}

function startDrag() {
  isDragging.value = true;
}

function endDrag(e: MouseEvent) {
  x.value = e.clientX - offsetX.value;
  y.value = e.clientY - offsetY.value;
  isDragging.value = false;
}
function handleOut() {
  isDragging.value = false;
}

async function handleDrag(e: MouseEvent) {
  x.value = e.clientX - offsetX.value;
  y.value = e.clientY - offsetY.value;
  if (isDragging.value) {
    const ctx = canvasRef.value!.getContext("2d")!;
    const bitmap = await createImageBitmap(imageRef.value, 0, 0, 300, 300);
    ctx.clearRect(0, 0, 300, 300);
    console.log(x.value, y.value);

    ctx.drawImage(bitmap, x.value - 150, y.value - 150);
  }
}

onMounted(() => {
  offsetX.value = canvasRef.value!.offsetLeft;
  offsetY.value = canvasRef.value!.offsetTop;
  console.log(offsetX.value);
  console.log(offsetY.value);
});

onBeforeUnmount(() => {});
</script>

<template>
  <div>
    <canvas
      ref="canvasRef"
      @mousedown="startDrag"
      @mouseup="endDrag"
      @mousemove="handleDrag"
      @mouseout="handleOut"
    ></canvas>
    <button type="button" @click="downloadImage">download image</button>
  </div>
</template>

<style lang="css" scoped>
div {
  display: flex;
  flex-direction: column;
  padding-bottom: 1rem;
}
canvas {
  margin-inline: auto;
  width: 300px;
  height: 300px;
  border-radius: 150px;
  border: 1px solid grey;
}
</style>
