<script setup lang="ts">
interface Props {
  image: File;
}
const props = defineProps<Props>();

const imageRef = toRef(props, "image");

const canvasRef = ref<HTMLCanvasElement>();
const previewImage = ref<string>("");

watch(imageRef, async () => {
  const b = await createImageBitmap(imageRef.value, 0, 0, 300, 300);
  canvasRef.value?.getContext("2d")?.drawImage(b, 0, 0);
});

onMounted(() => {});

onBeforeUnmount(() => {});
</script>

<template>
  <div>
    <canvas ref="canvasRef"></canvas>
  </div>
</template>

<style lang="css" scoped>
div {
  display: flex;
  padding-bottom: 1rem;
}
canvas {
  margin-inline: auto;
  width: 300px;
  height: 300px;
  border: 1px solid grey;
}
</style>
