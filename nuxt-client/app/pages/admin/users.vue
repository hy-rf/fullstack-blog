<script setup>
import { ref, onMounted } from "vue";

const users = ref([]);

onMounted(async () => {
  try {
    const res = await fetch("/api/users");
    if (!res.ok) throw new Error("Failed to fetch users");
    const data = await res.json();
    users.value = data;
  } catch (err) {
    console.error(err);
  }
});
</script>

<template>
  <div class="container">
    <h2 class="title">User List</h2>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Username</th>
          <th>Email</th>
          <th>Roles</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>
            {{ user.id }}
          </td>
          <td>
            <nuxt-link :to="`/user/${user.id}`">{{ user.username }}</nuxt-link>
          </td>
          <td>{{ user.email }}</td>
          <td>
            <ul class="role-list">
              <li v-for="role in user.roles" :key="role.id">{{ role }}</li>
            </ul>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
.container {
  margin-bottom: 5rem;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

th,
td {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: left;
}

thead {
  background-color: #f4f4f4;
}

ul {
  padding-left: 16px;
  margin: 0;
}

li {
  list-style-type: decimal;
}

.role-list {
  list-style: none;
}
</style>
