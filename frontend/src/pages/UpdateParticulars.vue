<template>
    <div class="my-particular">
      <h2 class="heading">Personal Particulars</h2>
    <hr class="divider" />
    <div class="fields-container">
      <div class="field full-width">
        <label for="residency_status">Residency Status</label>
        <select id="residency_status" v-model="residencyStatus">
          <option v-for="(option, index) in residencyStatusOptions" :value="option" :key="index">{{ option }}</option>
        </select>

        <label for="marital_status">Marital Status</label>
        <select id="marital_status" v-model="maritalStatus">
          <option v-for="(option, index) in maritalStatusOptions" :value="option" :key="index">{{ option }}</option>
        </select>

        <label for="employment_status">Employment Status</label>
        <select id="employment_status" v-model="employmentStatus">
          <option v-for="(option, index) in employmentStatusOptions" :value="option" :key="index">{{ option }}</option>
        </select>

        <label for="age_range">Age Range</label>
        <select id="age_range" v-model="ageRange">
          <option v-for="(option, index) in ageRangeOptions" :value="option" :key="index">{{ option }}</option>
        </select>

        <label for="ownership_history">Ownership History</label>
        <select id="ownership_history" v-model="ownershipHistory">
          <option v-for="(option, index) in ownershipHistoryOptions" :value="option" :key="index">{{ option }}</option>
        </select>
      </div>
      <div class="input">
        <label for="income">Household Monthly Income</label>
        <input type="number" id="income" min="0" max="100000" v-model.number="income" />
        <p v-if="income < 0" class="error-message" style="color: red">Income cannot be less than 0.</p>
      </div>
      <div class="field">
        <label>Family Nucleus</label>
        <div class="radio-list">
          <label>
            <input type="radio" v-model="familyNucleus" value="Single" />
            Single
          </label>
          <label>
            <input type="radio" v-model="familyNucleus" value="Couple" />
            Couple
          </label>
          <label>
            <input type="radio" v-model="familyNucleus" value="Extended Family" />
            Extended Family
          </label>
          <label>
            <input type="radio" v-model="familyNucleus" value="With Children" />
            With Children
          </label>
        </div>
      </div>
      <div class="buttons">
        <button class="cancel-button" @click="cancel">Cancel</button>
        <button class="update-button" @click="update">Update</button>
      </div>
    </div>
  </div>
</template>


<script lang="ts">
import axios from 'axios'
import { defineComponent } from 'vue'
import { useSessionsStore } from 'stores/sessions'
import { useAccountStore } from 'stores/accounts'
import { usePersonalParticularStore} from "stores/personal-particular";
import { mapStores } from "pinia"

export default defineComponent({
  name: 'UpdateParticulars',
  data() {
    return {
      residencyStatusOptions: ["Citizen", "Permanent Resident", "Work Visa Holder", "Student Visa Holder", "Foreigner"],
      maritalStatusOptions: ["Single", "Married", "Divorced", "Widowed"],
      employmentStatusOptions: ["Employed", "Self-Employed", "Unemployed", "Retired", "Other"],
      ageRangeOptions: ["Below 18", "18 - 25", "26 - 40", "41 - 60", "60 and above"],
      ownershipHistoryOptions: ["First Time Buyer", "Others"],
      residencyStatus: '',
      maritalStatus: '',
      employmentStatus: '',
      ageRange: '',
      ownershipHistory: '',
      income: '',
      familyNucleus: '',
    };
  },
  computed: {
    ...mapStores(useSessionsStore, useAccountStore, usePersonalParticularStore),
  },
  async created() {
    try {
      const userId = this.sessionsStore.sessionAccount?.accountId
      const response = await axios.get('/api/v1/accounts/' + userId + '/viewPersonalParticular');

    } catch (error) {
      console.error('Error fetching data:', error);
    }
  },
  methods: {
    cancel() {
      this.$router.push('/viewparticulars');
    },
    async update() {
      try {
        const userId = this.sessionsStore.sessionAccount?.accountId
        const data = {
          personalParticular: {
            residencyStatus: this.residencyStatus,
            maritalStatus: this.maritalStatus,
            employmentStatus: this.employmentStatus,
            ageRange: this.ageRange,
            ownershipHistory: this.ownershipHistory,
            income: this.income,
            familyNucleus: this.familyNucleus
          }
        };
        console.log(userId)
        if(userId){
          await this.AccountStore.updatePersonalParticular(userId, data.personalParticular);
          console.log(data)
          this.$router.push('/viewparticulars');
        }
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    }
  },
});
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
