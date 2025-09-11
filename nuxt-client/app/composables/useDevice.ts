export default function useDevice(breakpoint = 768) {
  const isClient = typeof window !== "undefined";
  const width = ref<number>(isClient ? window.innerWidth : 0);
  const isMobile = computed(() => width.value < breakpoint);
  const isDesktop = computed(() => width.value >= breakpoint);

  let rafId: number | null = null;

  function updateWidth() {
    if (!isClient) return;
    width.value = window.innerWidth;
  }

  function handleResize() {
    if (!isClient) return;
    if (rafId !== null) cancelAnimationFrame(rafId);
    rafId = requestAnimationFrame(updateWidth);
  }

  onMounted(() => {
    if (!isClient) return;
    window.addEventListener("resize", handleResize, { passive: true });
    window.addEventListener("orientationchange", handleResize, {
      passive: true,
    });
    updateWidth();
  });

  onBeforeUnmount(() => {
    if (!isClient) return;
    window.removeEventListener("resize", handleResize);
    window.removeEventListener("orientationchange", handleResize);
    if (rafId !== null) cancelAnimationFrame(rafId);
  });

  return {
    width,
    isClient,
    isMobile,
    isDesktop,
    breakpoint,
    updateWidth,
  };
}
