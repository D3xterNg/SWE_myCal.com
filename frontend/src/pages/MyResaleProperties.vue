<template>
  <div class="q-mx-xl" absolute-centre>
    <div>
      <p class="text-h6">
        Booked Properties
        <q-separator inset />
      </p>
    </div>

    <div>
      <q-inner-loading :showing="properties == null" />
      <template v-if="!!properties && properties.length">
        <q-card>
          <q-card-section outlined>
            <q-card-section outlined>
              <div class="q-px-sm">
                <div class="q-pb-md">
                  <q-input filled v-model="search" label="Search" type="text">
                    <template v-slot:append>
                      <q-icon name="search" />
                    </template>
                  </q-input>
                </div>
                <q-card>
                  <q-card-section>
                    <q-list separator>
                      <div
                        v-for="property in filteredPosts"
                        :key="property.id"
                        :property-id="property"
                      >
                        <property-test :property="property">{{
                          property.title
                        }}</property-test>
                      </div>
                    </q-list>
                  </q-card-section>
                </q-card>
              </div>
            </q-card-section>
          </q-card-section>
        </q-card>
      </template>
      <p v-if="!!properties && !properties.length" style="text-align: center">
        No Purchased Property
      </p>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { mapStores } from "pinia";

import { useSessionsStore } from "stores/sessions";
import { usePropertyStore } from "stores/property";
import Account from "src/interface/Account";
import Property from "src/interface/Property";
import { useAccountStore } from "stores/accounts";
import PropertyList from "src/components/PropertyList.vue";
import PropertyTest from "src/components/PropertyTest.vue";

export default defineComponent({
  name: "MyResaleProperties",
  data() {
    return {
      search: "",
      Price: null,
      room: null,
      storey: null,
      date: null,
      properties: null as string[] | null,
      propertyObj: null as Property[] | null,
    };
  },
  async created() {
    if (this.sessionsStore.sessionAccount == null) {
      this.$router.replace(
        "/login?from=" + encodeURIComponent("/myresaleprop")
      );
      return;
    }
    this.properties = await this.AccountStore.getBookedProperty(
      this.sessionsStore.sessionAccount.accountId
    );
    this.propertyObj = await this.propertyStore.getPropertiesByIds(
      this.properties
    );
  },
  components: { PropertyTest },
  computed: {
    ...mapStores(useAccountStore, useSessionsStore, usePropertyStore),

    // filter function for search bar, filters by title or location
    filteredPosts() {
      if (this.propertyObj != null) {
        return this.propertyObj.filter(
          (property) =>
            property.title.toLowerCase().includes(this.search.toLowerCase()) ||
            property.location.toLowerCase().includes(this.search.toLowerCase())
        );
      }
    },
  },
});
</script>

<style scoped>
#container {
  width: 100%;
  height: 100px;
  margin: auto;
  display: flex;
  justify-content: space-between;
}

.box {
  width: 20%;
  height: 80%;
  color: white;
  font-size: 2rem;
  border: 4px solid white;
}

img {
  width: 150px;
  height: 120px;
}
</style>
