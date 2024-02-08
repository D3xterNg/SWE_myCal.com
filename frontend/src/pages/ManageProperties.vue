<template>
  <main>
    <div class="jumbotron">
      <h4>Best place for the<br>best price</h4>

    </div>
    <div style=" padding: 2rem;">
      <div style="display: flex; justify-content: space-between;">
        <h4>Manage Properties</h4>
        <div style="display: grid; grid-template-rows: auto max-content auto;;">
          <div></div><q-btn to="/property/create" color="primary">Create</q-btn>
          <div></div>
        </div>
      </div>
<!--      <q-input v-model:model-value="search" label="Search">-->
<!--        <template v-slot:append>-->
<!--          <q-icon name="search" />-->
<!--        </template>-->
<!--      </q-input>-->
    </div>
    <q-card>
      <div style="display: flex; justify-content: flex-end;"><q-btn color="red" style="margin: 1rem;"
          @click="deleteMode = !deleteMode">Delete</q-btn></div>

      <q-carousel keep-alive height="500px" v-if="properties != null" swipeable animated v-model="slide"
        control-color="blue" navigation padding arrows class="text-purple rounded-borders">
        <q-carousel-slide v-for="(property1, key) in properties" :name="key" class=" no-wrap">
          <div class="q-pa-md row items-start q-gutter-md" style="display: grid;grid-template-columns: 1fr 1fr 1fr;">
            <property-card :delete-mode="deleteMode" :edit-mode="true" v-for="(property2, key2) in property1" :key="property2.id"
              :property="property2" :property-id="property2.id"
              @deleteClick="confirmDelete = true; deletingId = property2.id; deletingIndex1 = key; deletingIndex2 = key2"
              @edit-click="$router.push({ path: '/property/edit', query: {id: property2.id} })"/>
          </div>
        </q-carousel-slide>

      </q-carousel>
      <q-inner-loading :showing="properties == null || loading" label="Please wait..." label-class="text-teal"
        label-style="font-size: 1.1em" />
      <q-btn outline style="width: 100%;">View All</q-btn>
    </q-card>
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
import { mapStores } from 'pinia'

import { useSessionsStore } from 'stores/sessions';
import { usePropertyStore } from 'stores/property'
import Account from 'src/interface/Account';
import Property from 'src/interface/Property'
import PropertyCard from 'components/PropertyCard.vue'

export default defineComponent({
  name: 'ManageProperties',
  data() {
    return {
      properties: null as Property[][] | null,
      slide: 0,
      search: '',
      deleteMode: false,
      confirmDelete: false,
      deletingId: '',
      deletingIndex1: 0,
      deletingIndex2: 0,
      loading: false
    }
  },
  components: { PropertyCard },
  computed: {
    ...mapStores(useSessionsStore, usePropertyStore)
  },
  async mounted() {
    if (this.sessionsStore.sessionAccount == null) {
      this.$router.replace({ path: '/login', query: { from: '/property/manage' } })
      return;
    }
    let tempProperties = await this.propertyStore.getPropertyBySellerId(this.sessionsStore.sessionAccount.accountId);
    this.properties = [];
    while (tempProperties.length) {
      this.properties.push(tempProperties.splice(0, 3))
    }

  },
  methods: {
    async deleteProperty() {
      if (!this.properties) return;
      try {
        this.loading = true;
        let status = await this.propertyStore.deleteProperty(this.deletingId);
        switch (status) {
          case 204:

            let length = this.properties.length - 1;
            this.properties[this.deletingIndex1].splice(this.deletingIndex2, 1);
            for (let i = this.deletingIndex1; i < length; ++i) {
              this.properties[i].push(this.properties[i + 1].shift() as Property);
            }
            if (this.properties[length].length == 0) {
              this.properties.pop();
            }
            let temp = this.properties;
            this.properties = null;
            this.properties = temp;
            console.log(this.properties);
          default:
            if (status >= 500) {
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

      } finally {
        this.loading = false;
      }
    }
  }
})
</script>
<style scoped>
main {
  margin: 1rem;
}

.jumbotron {
  position: relative;
  /*background-color: blue;*/
  background-image: url("/statics/6.jpg");
  height: 250px;
  display: flex;
  justify-content: center;
}

.jumbotron h4 {
  position: absolute;
  top: 25%;
  left: 25%;
  transform: translate(-50%, -50%);
  color: white;
}
</style>
