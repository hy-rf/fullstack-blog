<script setup lang="ts">
const router = useRouter();
const { t } = useI18n();

const content = ref("");
const tagInput = ref("");
const tags = ref<string[]>(["test"]);

const addTag = () => {
  if (tagInput.value.trim() === "") {
    return;
  }
  if (tagInput.value && !tags.value.includes(tagInput.value)) {
    tags.value.push(tagInput.value.trim());
    tagInput.value = "";
  }
};

const removeTag = (tag: string) => {
  tags.value = tags.value.filter((t) => t !== tag);
};

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
        tags: tags.value,
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

const tagInputRef = ref<HTMLInputElement | null>(null);
</script>

<template>
  <h1>{{ t("new.title") }}</h1>
  <div>
    <textarea v-model="content" placeholder="Enter post content" />
    <div>
      <input ref="tagInputRef" v-model="tagInput" placeholder="Enter tag" />
      <button @click="addTag" id="add-tag-button">Add Tag</button>
    </div>
    <div>
      <span v-for="tag in tags" :key="tag" class="tags">
        {{ tag }}
        <button @click="removeTag(tag)" class="remove-tag-buttons">x</button>
      </span>
    </div>
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
textarea {
  margin-bottom: 1rem;
  padding: 0.5rem;
  width: 100%;
  resize: vertical;
}
textarea::-webkit-resizer {
  background: transparent;
}

input {
  border-radius: 0.3rem;
  padding: 0.5rem;
  width: 10rem;
  height: 100%;
  z-index: 1;
  position: relative;
  border: 1px solid grey;
  background-color: white;
  outline: none;
  padding: 0.5rem;
}

#add-tag-button {
  padding: 0.5rem;
  background-color: #2196f3;
  color: white;
  display: inline-block;
}

span.tags {
  display: inline-block;
  margin-top: 0.1rem;
  padding-left: 1rem;
  background-color: #e0e0e0;
  border-radius: 4px;
}

button.remove-tag-buttons {
  margin: 0;
  padding: 1rem;
  background: none;
  border: none;
  color: red;
  cursor: pointer;
  transition: background-color 300ms ease-in;
}
button.remove-tag-buttons:hover {
  background-color: #ff0000;
  transition: background-color 300ms ease-out;
}
</style>
