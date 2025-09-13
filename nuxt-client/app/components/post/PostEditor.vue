<script setup lang="ts">
import type PostToEditViewModel from "./PostToEditViewModel";

const props = defineProps<{
  postToEdit: PostToEditViewModel;
  rootPostId: number;
  postId: number;
  refresh: () => void;
}>();

const content = ref("");
const tagInput = ref("");
const tags = ref<string[]>([]);

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
  if (!content.value.trim()) {
    alert("Content is missing!");
    return;
  }

  try {
    const response = await fetch("/api/post", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        content: content.value.trim(),
        tags: tags.value,
        rootPostId: props.rootPostId,
        postId: props.postId,
      }),
    });

    if (!response.ok) {
      throw new Error("Failed to post");
    }

    const result = await response.text();
    alert(result);
    content.value = "";
    tagInput.value = "";
    tags.value = [];
    // Refresh child posts of root post
    props.refresh();
  } catch (error) {
    console.error(error);
    alert("Error creating post");
  }
};
onMounted(async () => {
  console.log(`root: ${props.rootPostId}, parent: ${props.postId}`);
});
</script>

<template>
  <form @submit.prevent="submitPost">
    <textarea v-model="content" placeholder="Enter post content" />
    <div>
      <input ref="tagInputRef" v-model="tagInput" placeholder="Enter tag" />
      <button id="add-tag-button" type="button" @click="addTag">Add Tag</button>
    </div>
    <div>
      <span v-for="tag in tags" :key="tag" class="tags">
        <span :key="tag" class="tag-name">
          {{ tag }}
        </span>
        <button
          type="button"
          class="remove-tag-buttons"
          @click="removeTag(tag)"
        >
          <Icon
            name="material-symbols:close-rounded"
            style="
              color: black;
              position: absolute;
              margin: auto;
              left: 0;
              bottom: 0;
            "
            size="24"
          />
        </button>
      </span>
    </div>
    <button id="submit-button" type="submit">Submit Post</button>
  </form>
</template>

<style lang="css" scoped>
form {
  display: flex;
  flex-direction: column;
}
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
  border-radius: 0.3rem;
}
textarea::-webkit-resizer {
  background: transparent;
}

#submit-button {
  margin-top: 1rem;
  background-color: #8bff9b;
  color: black;
  margin-left: auto;
  border-radius: 0.3rem;
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
  background-color: white;
  color: black;
  display: inline-block;
}

span.tags {
  display: inline-block;
  border-radius: 0.3rem;
  height: 26px;
  position: relative;
  margin-right: 1rem;
  margin-top: 1rem;
}

.tag-name {
  display: inline-block;
  padding-inline: 1rem;
  vertical-align: super;
  background-color: #ffffff;
  box-sizing: border-box;
  line-height: 26.5px;
  border-top-left-radius: 0.3rem;
  border-bottom-left-radius: 0.3rem;
}

button.remove-tag-buttons {
  /* box-sizing: content-box; */
  margin: 0;
  padding: 0;
  background: #00000080;
  border: 1px solid rgba(0, 0, 0, 0.2);
  color: red;
  cursor: pointer;
  width: 26px;
  height: 26px;
  position: relative;
  vertical-align: baseline;
  top: 0.5px;
  border-top-right-radius: 0.3rem;
  border-bottom-right-radius: 0.3rem;
}
</style>
