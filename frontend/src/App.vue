<template>
    <div style="flex: auto; display: grid; grid-template-rows: auto min-content; padding: 0;">
      <div style="display: flex;flex-direction: column;">
        <router-view />
      </div>
    <footer style="background-color: #f9f9f9;text-align: center;">
      <p>&copy; 2023 MyCal. All rights reserved.</p>
    </footer>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'App'
});
</script>

