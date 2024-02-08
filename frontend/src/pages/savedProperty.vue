<template>
  <main>
    <div style="min-height: 1rem;"></div>
    <div>
      <q-card class="card">
        <h4>Saved Property</h4>
        <q-inner-loading :showing="savedProperty == null" />
        <template v-if="!!savedProperty && savedProperty.length">
          <div style="display: flex; justify-content: flex-end;"><q-btn color="red" style="margin: 1rem;"
              @click="deleteMode = !deleteMode">Delete</q-btn></div>
          <div style="display: grid; grid-template-columns: 1fr 1fr 1fr ;grid-gap: 1rem;">
            <property-card :delete-mode="deleteMode" v-for="(property, key) in savedProperty.slice(0, 3)"
              @delete-click="confirmDelete = true; deletingId = property; deletingIndex = key" :key="property"
              :property-id="property" />
            <template v-if="viewAll">
              <property-card :delete-mode="deleteMode" v-for="(property, key) in savedProperty.slice(3)"
                @delete-click="confirmDelete = true; deletingId = property; deletingIndex = (key + 3)" :key="property"
                :property-id="property" />
            </template>
          </div>
          <q-btn v-if="!viewAll" outline style="width: 100%;" @click="viewAll = true">View All</q-btn>
        </template>
        <p v-if="!!savedProperty && !savedProperty.length" style="text-align: center;">No saved Property Found</p>

      </q-card>
    </div>
    <div style="min-height: 1rem;"></div>
  </main>
  <q-dialog v-model="confirmDelete" persistent>
    <q-card>
      <q-card-section class="row items-center">
        <q-avatar icon="logout" color="red" text-color="white" />
        <span class="q-ml-sm">Are you sure you want to delete</span>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn label="No" color="primary" v-close-popup />
        <q-btn label="Delete" color="red" @click="deleteProperty" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { mapStores } from 'pinia';

import { useAccountStore } from 'stores/accounts'
import { useSessionsStore } from 'stores/sessions'
import PropertyCard from 'components/PropertyCard.vue'

export default defineComponent({
  // name: 'PageName'
  data() {
    return {
      savedProperty: null as string[] | null,
      viewAll: false,
      deleteMode: false,
      deletingId: '',
      deletingIndex: 0,
      confirmDelete: false
    }
  },
  async created() {
    if (this.sessionsStore.sessionAccount == null) {
      this.$router.replace('/login?from=' + encodeURIComponent('/savedProperty'))
      return;
    }
    try {
      let res = await this.AccountStore.getSavedProperty(this.sessionsStore.sessionAccount.accountId);
      switch (typeof res) {
        case "object":
          this.savedProperty = res;
          break;
        case "number":
          if (res >= 500) {
            this.$q.notify({
              type: 'negative',
              message: 'Server Error'
            });
          }
      }
    } catch (error) {
      this.$q.notify({
            type: 'negative',
            message: 'Connection Error, PLease check your Network'
        });
    }

  },
  components: { PropertyCard },
  methods: {
    async deleteProperty() {
      let status = await this.AccountStore.deleteSavedProperty(this.sessionsStore.sessionAccount?.accountId as string, this.deletingId);
      try {
        if (status === 204) {
          this.savedProperty?.splice(this.deletingIndex, 1);
        } else if (status >= 500) {
          this.$q.notify({
            type: 'negative',
            message: 'Server Error'
          });
        }
      } catch (error) {
        this.$q.notify({
          type: 'negative',
          message: 'Connection Error, PLease check your Network'
        });
      }
    }
  },
  computed: {
    ...mapStores(useAccountStore, useSessionsStore)
  }
})
</script>

<style scoped>
main {
  flex: auto;
  display: grid;
  grid-template-rows: auto min-content auto;
  background-color: darkblue;
  position: relative;
}

main>div {
  display: flex;
  justify-content: center;
}

.card {

  padding: 1rem;
}

.card h4 {
  text-align: center;
}</style>
