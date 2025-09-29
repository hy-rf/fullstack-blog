<script setup lang="ts">
import compressAndConvertImage from "~/utils/ConvertImage";

const router = useRouter();
const { t } = useI18n();

const content = ref("");
const tagInput = ref("");
const tags = ref<string[]>([]);
const images = ref<File[]>([]);
const IMAGE_QUALITY = 0.1;
const isPrevButtonDisabled = ref(false);
const isNextButtonDisabled = ref(false);
const scrollTime = 400;

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
  const wasNoImages = images.value.length == 0;
  const files = (event.target as HTMLInputElement).files;
  if (files) {
    // console.log(files.length); // prints 1
    for (const file of files) {
      images.value.push(file);
    }
  }
  setTimeout(
    () => {
      last();
    },
    wasNoImages ? 100 : 0,
  );
};

const submitPost = async () => {
  const imageConversionResult = images.value.map((f) =>
    compressAndConvertImage(f, "image/webp", IMAGE_QUALITY),
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
  router.push("/post/" + result);
};

const tagInputRef = ref<HTMLInputElement | null>(null);

const getObjectURL = (file: File) => {
  return window.URL.createObjectURL(file);
};

const imageScrollView = ref<HTMLDivElement>();

const prev = () => {
  isPrevButtonDisabled.value = true;
  setTimeout(() => {
    isPrevButtonDisabled.value = false;
  }, scrollTime);
  const width = imageScrollView.value?.getBoundingClientRect().width;
  if (!width) return;
  const options: ScrollToOptions = {
    left: -1 * width,
    behavior: "smooth",
  };
  imageScrollView.value?.scrollBy(options);
};
const next = () => {
  isNextButtonDisabled.value = true;
  setTimeout(() => {
    isNextButtonDisabled.value = false;
  }, scrollTime);
  const width = imageScrollView.value?.getBoundingClientRect().width;
  if (width) {
    const options: ScrollToOptions = {
      left: width,
      behavior: "smooth",
    };
    imageScrollView.value?.scrollBy(options);
  }
};

const last = () => {
  isPrevButtonDisabled.value = true;
  setTimeout(() => {
    isPrevButtonDisabled.value = false;
  }, scrollTime);
  isNextButtonDisabled.value = true;
  setTimeout(() => {
    isNextButtonDisabled.value = false;
  }, scrollTime);
  console.log("Start scroll to last");

  const width = imageScrollView.value?.getBoundingClientRect().width;
  if (width) {
    const options: ScrollToOptions = {
      left: width * images.value.length,
      behavior: "smooth",
    };
    imageScrollView.value?.scrollBy(options);
  }
  console.log("End scroll to last");
};
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
        <div style="position: relative">
          <button
            v-if="images.length > 1"
            type="button"
            class="previous-image-button"
            :disabled="isPrevButtonDisabled"
            @click="prev"
          >
            <Icon name="mdi-light:chevron-left" />
          </button>
          <button
            v-if="images.length > 1"
            type="button"
            class="next-image-button"
            :disabled="isNextButtonDisabled"
            @click="next"
          >
            <Icon name="mdi-light:chevron-right" />
          </button>
          <div ref="imageScrollView" class="image-preview-container">
            <div
              v-for="(img, index) in images"
              :key="index"
              class="image-wrapper"
            >
              <img :src="getObjectURL(img)" alt="Uploaded Image" />
            </div>
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
  position: relative;
  display: flex;
  margin-block: 1rem;
  overflow-x: auto;
  border-radius: 0.5rem;
  border: 0;
  /* background-color: black; */
}

.image-wrapper {
  min-width: 100%;
  max-height: 50dvh;
  padding-inline: 10%;
  background-color: black;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border: 0;
}

.previous-image-button {
  position: absolute;
  margin: 0;
  padding: 0;
  height: 100%;
  z-index: 99;
  width: 10%;
  background-color: #999999aa;
  border-bottom-left-radius: 0.5rem;
  border-top-left-radius: 0.5rem;
}
.next-image-button {
  position: absolute;
  margin: 0;
  padding: 0;
  height: 100%;
  right: 0;
  z-index: 99;
  width: 10%;
  background-color: #999999aa;
  border-bottom-right-radius: 0.5rem;
  border-top-right-radius: 0.5rem;
}
.previous-image-button:disabled,
.next-image-button:disabled {
  background-color: #9999994c;
}
</style>
