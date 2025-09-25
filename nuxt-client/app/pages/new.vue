<script setup lang="ts">
import compressAndConvertImage from "~/utils/ConvertImage";

const router = useRouter();
const { t } = useI18n();

const content = ref("");
const tagInput = ref("");
const tags = ref<string[]>([]);
const images = ref<File[]>([]);
const MAX_IMAGE_SIZE = 25 * 1024 * 1024;

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

const handleImageUpload = (event: Event) => {
  const files = (event.target as HTMLInputElement).files;
  if (files) {
    for (const file of files) {
      images.value.push(file);
    }
  }
};

const submitPost = async () => {
  const imageConversionResult = images.value.map((f) =>
    compressAndConvertImage(f, "image/webp", 0.1),
  );
  const imageBase64Strings: string[] = await Promise.all(imageConversionResult);

  if (!content.value) {
    alert("Title or content is missing!");
    return;
  }
  const response = await fetch("/api/post", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      content: content.value,
      tags: tags.value,
      imagesBase64Strings: imageBase64Strings,
    }),
  });

  if (!response.ok) {
    throw new Error("Failed to post");
  }

  const result = await response.text();
  // await uploadImages(parseInt(result));
  alert(result);
  router.push("/");
};

const uploadImages = async (postId: number) => {
  images.value.forEach(async (file) => {
    if (!file.type.startsWith("image")) {
      alert(t("me.update.avatar.type_error"));
      return;
    }
    if (file.size > MAX_IMAGE_SIZE) {
      alert(t("me.update.avatar.size_error"));
      return;
    }
    const fileToUpload: File = new File(
      [file],
      `${file.lastModified.toString()}.${file.name.split(".")[1]}`,
      { type: file.type },
    );
    const formData = new FormData();
    formData.append("file", fileToUpload);
    formData.append("postId", postId.toString());
    await fetch(`${useRuntimeConfig().public.GATEWAY_URL}/post-image`, {
      method: "post",
      body: formData,
      credentials: "include",
    });
  });
};

const tagInputRef = ref<HTMLInputElement | null>(null);

const getObjectURL = (file: File) => window.URL.createObjectURL(file);
</script>

<template>
  <div>
    <h1 class="title">{{ t("new.title") }}</h1>
    <form @submit.prevent="submitPost">
      <textarea v-model="content" placeholder="Enter post content" />
      <div>
        <input ref="tagInputRef" v-model="tagInput" placeholder="Enter tag" />
        <button id="add-tag-button" type="button" @click="addTag">
          Add Tag
        </button>
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
      <div id="image-upload-container">
        <label
          >Upload
          <input type="file" multiple @change="handleImageUpload" />
        </label>
        <div class="image-preview-container">
          <div
            v-for="(img, index) in images"
            :key="index"
            class="image-wrapper"
          >
            <img :src="getObjectURL(img)" alt="Uploaded Image" />
          </div>
        </div>
      </div>
      <button id="submit-button" type="submit">Submit Post</button>
    </form>
  </div>
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
  outline: none;
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

/* File upload button */
label {
  display: block;
  width: 15rem;
  margin-inline: auto;
  text-align: center;
  border: 1px solid #888888;
  padding: 0.3rem 1rem;
  span {
    display: block;
    padding-block: 0.3rem;
  }
  span:hover {
    background-color: #888888;
    color: #eeeeee;
  }
  input {
    display: none;
  }
}
label:hover {
  background-color: #0000004a;
}

/* Image Preview Styles */
.image-preview-container {
  display: flex;
  gap: 20%;
  margin-top: 1rem;
  overflow-x: auto;
  & > div:first-child {
    margin-left: 10%;
  }
  & > div:last-child {
    margin-right: 10%;
  }
}

.image-wrapper {
  min-width: 80%;
  height: 50dvh;
  background-color: black;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  border-radius: 0.5rem;
}

.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}
</style>
