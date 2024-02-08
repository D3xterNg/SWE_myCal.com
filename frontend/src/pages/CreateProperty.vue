<template>
  <main>
    <q-form class="create-property" @submit.prevent="create">
      <div>
        <h2 class="heading">Create Property</h2>
      </div>
      <hr class="divider" />
      <div class="fields-container">
        <div id="textinfo">
          <div class="input-field">
            <q-input :loading="loading" label="Title" v-model="title" :rules="[val => !!val || 'Title required']" />
          </div>
          <div class="input-field">
            <q-input :loading="loading" type="textarea" label="Description" v-model="description" :rules="[val => !!val || 'Description required']" />
            </div>
          <div class="input-field">
            <q-input :loading="loading" label="Location" v-model="location" :rules="[val => !!val || 'Location required']" />
          </div>
          <div class="input-field">
            <q-select :loading="loading" label="Flat Type" :options="flatTypeOptions" map-value emit-value v-model="flatType" :rules="[val => !!val || 'Flat Type required']" />
          </div>
          <div class="input-field">
            <q-input :loading="loading" label="Storey" :min="1" :max="50" v-model="storey"
              :rules="[value => (value !== null && value >= 1 && value <= 50) || 'Storey must be between 1 and 50']"
              type="number" required
            />
          </div>
          <div class="input-field">
            <q-input :loading="loading" label="Commencement Date"  v-model="commenceDate" type="date"  :rules="[val => !!val || 'Commencement Date required']" />
          </div>
        </div>
        <div id="textinfo">
          <div class="input-field">
            <q-input :loading="loading" label="No. of Bathrooms" min="0" max="5"  v-model="noOfBathrooms"
                     :rules="[value => (value !== null && value >= 1 && value <= 5) || 'number of bathrooms must be between 1 and 5']"
                     type="number" required />
          </div>
          <div class="input-field">
            <q-input :loading="loading" label="No. of Bedrooms"  min="0" v-model="noOfBedrooms"
                     :rules="[value => (value !== null && value >= 1 && value <= 5) || 'number of bedrooms must be between 1 and 5']"
                     type="number" required />
          </div>
          <div class="input-field">
            <q-input :loading="loading" label="Price" v-model="price"  min="0"
                     :rules="[value => (value !== null && value > 0) || 'Price must be greater than 0']"
                     type="number" required >
              <template v-slot:prepend>
                $
              </template>
            </q-input>
          </div>
          <div class="input-field">
            <q-input :loading="loading" label="Size"  v-model="size" min="0"
                     :rules="[value => (value !== null && value > 0) || 'Size must be greater than 0']"
                     type="number" required >
              <template v-slot:append>
                sqm
              </template>
            </q-input>
          </div>
        </div>
        <div id="visualinfo">
          <div class="input-field">
            <q-file :loading="loading" label="Upload Image" v-model="photo" accept="image/*" :max-file-size="1048576" :rules="[val => !!val || 'Upload Image required']" >
              <template v-slot:prepend>
                <q-icon name="cloud_upload" @click.stop.prevent />
              </template>
            </q-file>
          </div>
        </div>
      </div>
      <div id="submit">
        <q-btn :loading="loading" type="submit" >Create</q-btn>
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
  name: 'CreateProperty',
  data() {
    return {
        title: '',
        description: '',
        location: '',
        flatType: '',
        storey: 1,
        commenceDate: '',
        image: [] as File[],
        noOfBedrooms: 0,
        noOfBathrooms: 0,
        price: 1,
        size: 1,
        photo: null as File | null,
      flatTypeOptions: [
        { label: '2 Room Flexi', value: '2 Room Flexi' },
        { label: '3 Room', value: '3 Room' },
        { label: '4 Room', value: '4 Room' },
        { label: '5 Room', value: '5 Room' },
        { label: '3 Gen Flats', value: '3 Gen Flats' },
      ],
      loading: false,
    };
  },

  setup() {
  const sessionsStore = useSessionsStore()
  return {
    sessionsStore
  }
},

  computed: {
    ...mapStores(usePropertyStore)
  },

  methods: {
    handleFileUpload(event: any) {
      this.photo = event.target.files[0];
      if (!this.photo) {
        return;
      }
    },
    async create() {
      console.log("hello");
      try {
        const userId = this.sessionsStore.sessionAccount?.accountId
        if (!userId) return;
        this.loading = true;
        const data:{property: Property} = {
          property: {
            id: "",
            title: this.title,
            description: this.description,
            location: this.location,
            flatType: this.flatType,
            storey: this.storey,
            commenceDate: this.commenceDate,
            numOfBedrooms: this.noOfBedrooms,
            numOfBathrooms: this.noOfBathrooms,
            price: this.price,
            size: this.size,
            photoExtension: ".webp",
            sellerId: userId
          }
        };
        let res = await this.propertyStore.createProperty(data.property, this.photo as File);
        switch (typeof res) {
          case 'string':
          this.$q.notify({
            type: 'positive',
            message: 'Successful Created Property'
          });
              this.$router.push('/property/manage');
            break;
          case 'number':
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
        this.loading = false;
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

.create-property {
  margin: 0 auto; /* Centered */
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
}

</style>
