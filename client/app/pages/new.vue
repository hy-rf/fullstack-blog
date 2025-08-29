<script setup>
const router = useRouter();

const content = ref("");

const submitPost = async () => {
  if (!content.value) {
    alert("Title or content is missing!");
    return;
  }

  try {
    const response = await fetch("/api/post", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        content: content.value,
      }),
    });

    if (!response.ok) {
      throw new Error("Failed to post");
    }

    const result = await response.text();
    alert(result);
    router.push("/");
  } catch (error) {
    console.error(error);
    alert("Error creating post");
  }
};
</script>

<template>
  <div>
    <input
      v-model="content"
      placeholder="Enter post title"
      style="margin-bottom: 1rem; padding: 0.5rem; width: 100%"
    />
    <button
      @click="submitPost"
      style="margin-top: 1rem; background-color: #4caf50; color: white"
    >
      Submit Post
    </button>
  </div>
</template>

<style scoped>
button {
  margin: 0.5rem;
  padding: 0.5rem 1rem;
  border: none;
  background-color: #f0f0f0;
  cursor: pointer;
}
button.is-active {
  background-color: #d0d0d0;
}
</style>
