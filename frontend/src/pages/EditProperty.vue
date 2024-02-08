<template>
  <main>
    <q-form class="edit-property" @submit.prevent="edit">
      <div>
        <h2 class="heading">Edit Property</h2>
      </div>
      <hr class="divider" />
      <div class="fields-container">
        <div id="textinfo">
          <div class="input-field">
            <q-input :loading="loading" label="Title" v-model="property.title" :rules="[val => !!val || 'Title required']"
              @change="propertyChanged.title = true" />
          </div>
          <div class="input-field">
            <q-input :loading="loading" type="textarea" label="Description" v-model="property.description"
              :rules="[val => !!val || 'Description required']" @change="propertyChanged.description = true" />
          </div>
          <div class="input-field">
            <q-input :loading="loading" label="Location" v-model="property.location"
              :rules="[val => !!val || 'Location required']" @change="propertyChanged.location = true" />
          </div>
          <div class="input-field">
            <q-select :loading="loading" label="Flat Type" :options="flatTypeOptions" map-value emit-value
              v-model="property.flatType" :rules="[val => !!val || 'Flat Type required']"
              @update:model-value="propertyChanged.flatType = true" />
          </div>
          <div class="input-field">
            <q-input :loading="loading" label="Storey" min="1" max="50" v-model="property.storey" type="number" required
              :rules="[value => (value !== null && value >= 1 && value <= 50) || 'Storey must be between 1 and 50']"
              @change="propertyChanged.storey = true" />
          </div>
          <div class="input-field">
            <q-input :loading="loading" label="Commencement Date" v-model="property.commenceDate" type="date"
              @change="propertyChanged.commenceDate = true" :rules="[val => !!val || 'Commencement Date required']" />
          </div>
        </div>
        <div id="textinfo">
          <div class="input-field">
            <q-input :loading="loading" label="No. of Bathrooms" min="0" max="5" v-model="property.numOfBathrooms" type="number"
                     :rules="[value => (value !== null && value >= 1 && value <= 5) || 'number of bathrooms must be between 1 and 5']"
              required @change="propertyChanged.numOfBathrooms = true" />
          </div>
          <div class="input-field">
            <q-input :loading="loading" label="No. of Bedrooms" min="0" max="5" v-model="property.numOfBedrooms" type="number"
                     :rules="[value => (value !== null && value >= 1 && value <= 5) || 'number of bedrooms must be between 1 and 5']"
              required @change="propertyChanged.numOfBedrooms = true" />
          </div>
          <div class="input-field">
            <q-input :loading="loading" label="Price" v-model="property.price" min="0"
                     :rules="[value => (value !== null && value > 0) || 'Price must be greater than 0']"
                     type="number" required
              @change="propertyChanged.price = true">
              <template v-slot:prepend>
                $
              </template>
            </q-input>
          </div>
          <div class="input-field">
            <q-input :loading="loading" label="Size" v-model="property.size" min="0"
                     :rules="[value => (value !== null && value > 0) || 'Price must be greater than 0']"
                     type="number" required
              @change="propertyChanged.size = true">
              <template v-slot:append>
                sqm
              </template>
            </q-input>
          </div>
        </div>
        <div id="visualinfo">
          <div class="input-field">
            <q-file :loading="loading" label="Upload Image" v-model="photo" accept="image/*" clearable :max-file-size="1048576">
              <template v-slot:prepend>
                <q-icon name="cloud_upload" @click.stop.prevent />
              </template>
            </q-file>
          </div>
        </div>
      </div>
      <div id="submit">
        <q-btn :loading="loading" type="submit">Save</q-btn>
      </div>
    </q-form>
  </main>
</template>

<script lang="ts">
import { mapStores } from 'pinia';
import { usePropertyStore } from 'stores/property';
import { useSessionsStore } from 'stores/sessions';
import { defineComponent } from 'vue';

import Property from 'src/interface/Property';

export default defineComponent({
  name: 'EditProperty',
  data() {
    return {
      property: {
        id: '',
        title: '',
        description: '',
        location: '',
        flatType: '',
        storey: 0,
        commenceDate: '',
        numOfBathrooms: 0,
        numOfBedrooms: 0,
        price: 1,
        size: 1,
        photoExtension: '',
        sellerId: ''
      } as Property,
      propertyChanged: {
        title: false,
        description: false,
        location: false,
        flatType: false,
        storey: false,
        commenceDate: false,
        numOfBathrooms: false,
        numOfBedrooms: false,
        price: false,
        size: false,
      },
      photo: null as File | null,
      propertyId: "",
      flatTypeOptions: [
        { label: '2 Room Flexi', value: '2 Room Flexi' },
        { label: '3 Room', value: '3 Room' },
        { label: '4 Room', value: '4 Room' },
        { label: '5 Room', value: '5 Room' },
        { label: '3 Gen Flats', value: '3 Gen Flats' },
      ],
      loading: false
    };
  },

  computed: {
    ...mapStores(usePropertyStore, useSessionsStore)
  },
  async created() {
    if (!this.sessionsStore.sessionAccount) {
      this.$router.replace({ path: '/login', query: { from: '/property/edit' } })
      return;
    }
    if (typeof this.$route.query.id !== "string") {
      this.$router.replace('/404');
      return;
    }
    this.propertyId = this.$route.query.id;
    try {
      this.loading = true;
      this.property = await this.propertyStore.getProperty(this.propertyId); // Make sure to type the response data
      if (this.sessionsStore.sessionAccount.accountId !== this.property.sellerId) {
        this.$router.replace('/403');
        return;
      }
    } catch (error) {
      console.error('Error fetching property details', error);
    } finally {
      this.loading = false;
    }
  },
  methods: {
    handleFileUpload(event: any) {
      this.photo = event.target.files[0];
      if (!this.photo) {
        return;
      }
    },
    async edit() {
      console.log("hello");
      try {
        const userId = this.sessionsStore.sessionAccount?.accountId
        if (!userId) return;
        this.loading = true;
        let tempProperties: Partial<Property> = {};
        Object.entries(this.propertyChanged).forEach(([key, value]) => {
          if (value) {
            /**@ts-ignore */
            tempProperties[key] = this.property[key]
          }
        })
        console.log(this.propertyChanged.flatType)
        console.log(tempProperties.flatType)
       let res = await this.propertyStore.editProperty(this.propertyId, tempProperties, this.photo as File);
        switch (res) {
          case 204:
            this.$q.notify({
              type: 'positive',
              message: 'Successful edited Property'
            });
            this.$router.push('/property/manage');
            break;
          default:
            if (res >= 500) {
              this.$q.notify({
                type: 'negative',
                message: 'Server Error'
              });
            }
            break;
        }
      } catch (error) {
        console.error('Error fetching data:', error);
        this.$q.notify({
            type: 'negative',
            message: 'Connection Error, PLease check your Network'
        });
      } finally {
        this.loading = false
      }

    },

  }
});

</script>

<style scoped>
/*main {*/
/*  display: flex;*/
/*  justify-content: center;*/
/*  align-items: center;*/
/*  min-height: 100vh;*/
/*  background-color: darkblue;*/
/*}*/

.edit-property {
  margin: 0 auto;
  /* Centered */
  padding: 15px;
  background-color: #ededed;
  width: 50%;
  height: 1200px;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}


.fields-container {
  padding: 10px;
  width: 100%;
  height: auto;
  background-color: #fff;
  box-shadow: inset 3px 0 8px -5px #d8d6d6, inset -3px 0 8px -5px #d8d6d6;
}

.heading {
  color: #1f0567;
  margin-bottom: 10px;
}

.divider {
  border: none;
  border-top: 2px solid #ccc;
  width: 100%;
  margin-top: 10px;
}

#textinfo {
  margin-right: 2rem;
}

.input-field {
  margin-bottom: 1.5rem;
}

input[type="date"],
input[type="number"],
textarea,
select {
  width: 100%;
  padding: 0.6rem;
  border-radius: 0.5rem;
  border: 1px solid #ddd;
}

button {
  padding: 0.7rem 1.5rem;
  border: none;
  border-radius: 0.5rem;
  background-color: #007bff;
  color: #fff;
  cursor: pointer;
}</style>
