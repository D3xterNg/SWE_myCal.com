<template>
  <div class="my-particular">
    <h2 class="heading">Personal Particulars</h2>
    <hr class="divider" />
    <div class="fields-container">
      <div>
        <p><strong>Residency Status:</strong> {{ personalParticulars.residencyStatus }}</p>
        <p><strong>Marital Status:</strong> {{ personalParticulars.maritalStatus }}</p>
        <p><strong>Employment Status:</strong> {{ personalParticulars.employmentStatus }}</p>
        <p><strong>Age Range:</strong> {{ personalParticulars.ageRange }}</p>
        <p><strong>Ownership History:</strong> {{ personalParticulars.ownershipHistory }}</p>
        <p><strong>Household Monthly Income:</strong> {{ personalParticulars.income }}</p>
        <p><strong>Family Nucleus:</strong> {{ personalParticulars.familyNucleus }}</p>
      </div>
      <div class="buttons">
        <router-link to="/updateparticulars">
          <button class="update-button">Update</button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import axios from 'axios'
import { defineComponent } from 'vue'
import { useSessionsStore } from 'stores/sessions'
import { mapStores } from "pinia"

export default defineComponent({
  name: 'ViewParticulars',
  data() {
    return {
      personalParticulars: {
        residencyStatus: 'NA',
        maritalStatus: 'NA',
        employmentStatus: 'NA',
        ageRange: 'NA',
        ownershipHistory: 'NA',
        income: 'NA',
        familyNucleus: 'NA'
      }
    };
  },
  async created() {
    try {
      const userId = this.sessionsStore.sessionAccount?.accountId
      if(userId){
        const response = await axios.get('/api/v1/accounts/' + userId + '/viewPersonalParticular');
        console.log(response)
        this.personalParticulars = response.data;}
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  },
  setup() {
    const sessionsStore = useSessionsStore()
    return {
      sessionsStore
    }
  },
  computed: {
    ...mapStores({
      sessionsStore: useSessionsStore,
    }),
  }
})
</script>

<style scoped>
/* Component container styles */
.my-particular {
  margin: 0 auto; /* Centered */
  padding: 15px;
  background-color: #ededed;
  width: 508px;
  height: 685px;
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

/* Heading and Divider */
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

/* Field styling */
.field {
  display: flex;
  flex-wrap: wrap;
  border-radius: 5px;
  margin-bottom: 10px;
}

.full-width {
  width: 100%;
}

.half-width {
  width: calc(50% - 10px);
}

/* Labels */
label {
  font-weight: bold;
  align-self: flex-start;
}

/* Dropdowns and Movable Bar (Age and Income) */
select,
input[type="range"] {
  width: 100%;
  padding: 5px;
  border: 1px solid #1f0567;
  border-radius: 5px;
}

/* Checkboxes and Buttons */
.checkboxes {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.checkbox-list {
  margin-left: 10px;
  justify-items: start;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.buttons {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.cancel-button,
.update-button {
  background-color: #ccc;
  color: #fff;
  padding: 10px 25px;
  border: none;
  border-radius: 20px;
  margin-right: 10px;
}

.update-button {
  background-color: #1f0567;
}
</style>
