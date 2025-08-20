<script setup>
const editor = useEditor({
  content: "",
  extensions: [TiptapStarterKit],
});

const title = ref(""); // Post title input

onBeforeUnmount(() => {
  unref(editor).destroy();
});

const router = useRouter();

const submitPost = async () => {
  if (!title.value || !editor.value) {
    alert("Title or content is missing!");
    return;
  }

  try {
    const response = await fetch("/api/post", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        // Include auth headers if needed (e.g., Bearer token)
      },
      body: JSON.stringify({
        title: title.value,
        content: editor.value.getHTML(),
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
      v-model="title"
      placeholder="Enter post title"
      style="margin-bottom: 1rem; padding: 0.5rem; width: 100%"
    />
    <div v-if="editor">
      <button
        @click="editor.chain().focus().toggleBold().run()"
        :disabled="!editor.can().chain().focus().toggleBold().run()"
        :class="{ 'is-active': editor.isActive('bold') }"
      >
        bold
      </button>
      <button
        @click="editor.chain().focus().toggleItalic().run()"
        :disabled="!editor.can().chain().focus().toggleItalic().run()"
        :class="{ 'is-active': editor.isActive('italic') }"
      >
        italic
      </button>
      <button
        @click="editor.chain().focus().toggleStrike().run()"
        :disabled="!editor.can().chain().focus().toggleStrike().run()"
        :class="{ 'is-active': editor.isActive('strike') }"
      >
        strike
      </button>
      <button
        @click="editor.chain().focus().toggleCode().run()"
        :disabled="!editor.can().chain().focus().toggleCode().run()"
        :class="{ 'is-active': editor.isActive('code') }"
      >
        code
      </button>
      <button @click="editor.chain().focus().unsetAllMarks().run()">
        clear marks
      </button>
      <button @click="editor.chain().focus().clearNodes().run()">
        clear nodes
      </button>
      <button
        @click="editor.chain().focus().setParagraph().run()"
        :class="{ 'is-active': editor.isActive('paragraph') }"
      >
        paragraph
      </button>
      <button
        @click="editor.chain().focus().toggleHeading({ level: 1 }).run()"
        :class="{ 'is-active': editor.isActive('heading', { level: 1 }) }"
      >
        h1
      </button>
      <button
        @click="editor.chain().focus().toggleHeading({ level: 2 }).run()"
        :class="{ 'is-active': editor.isActive('heading', { level: 2 }) }"
      >
        h2
      </button>
      <button
        @click="editor.chain().focus().toggleHeading({ level: 3 }).run()"
        :class="{ 'is-active': editor.isActive('heading', { level: 3 }) }"
      >
        h3
      </button>
      <button
        @click="editor.chain().focus().toggleHeading({ level: 4 }).run()"
        :class="{ 'is-active': editor.isActive('heading', { level: 4 }) }"
      >
        h4
      </button>
      <button
        @click="editor.chain().focus().toggleHeading({ level: 5 }).run()"
        :class="{ 'is-active': editor.isActive('heading', { level: 5 }) }"
      >
        h5
      </button>
      <button
        @click="editor.chain().focus().toggleHeading({ level: 6 }).run()"
        :class="{ 'is-active': editor.isActive('heading', { level: 6 }) }"
      >
        h6
      </button>
      <button
        @click="editor.chain().focus().toggleBulletList().run()"
        :class="{ 'is-active': editor.isActive('bulletList') }"
      >
        bullet list
      </button>
      <button
        @click="editor.chain().focus().toggleOrderedList().run()"
        :class="{ 'is-active': editor.isActive('orderedList') }"
      >
        ordered list
      </button>
      <button
        @click="editor.chain().focus().toggleCodeBlock().run()"
        :class="{ 'is-active': editor.isActive('codeBlock') }"
      >
        code block
      </button>
      <button
        @click="editor.chain().focus().toggleBlockquote().run()"
        :class="{ 'is-active': editor.isActive('blockquote') }"
      >
        blockquote
      </button>
      <button @click="editor.chain().focus().setHorizontalRule().run()">
        horizontal rule
      </button>
      <button @click="editor.chain().focus().setHardBreak().run()">
        hard break
      </button>
      <button
        @click="editor.chain().focus().undo().run()"
        :disabled="!editor.can().chain().focus().undo().run()"
      >
        undo
      </button>
      <button
        @click="editor.chain().focus().redo().run()"
        :disabled="!editor.can().chain().focus().redo().run()"
      >
        redo
      </button>
    </div>
    <TiptapEditorContent :editor="editor" />
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
