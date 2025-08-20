<script setup>
import { ref, onMounted } from "vue";

const users = ref([]);

onMounted(async () => {
  try {
    const res = await fetch("/api/users");
    if (!res.ok) throw new Error("Failed to fetch users");
    const data = await res.json();
    users.value = data.content
  } catch (err) {
    console.error(err);
  }
});
</script>

<template>
  <div class="container">
    <h1>User List</h1>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Username</th>
          <th>Email</th>
          <th>Full Name</th>
          <th>Is Active</th>
          <th>Roles</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.fullName }}</td>
          <td>{{ user.isActive ? "Yes" : "No" }}</td>
          <td>
            <ul>
              <li v-for="role in user.roles" :key="role.id">{{ role.name }}</li>
            </ul>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
.container {
  max-width: 900px;
  margin: 20px auto;
  padding: 10px;
  font-family: sans-serif;
}

h1 {
  text-align: center;
  margin-bottom: 20px;
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
  list-style-type: disc;
}
</style>
