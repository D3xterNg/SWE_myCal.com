

<script lang="ts">
import { defineComponent } from 'vue'
import { mapStores } from 'pinia'

import { useAccountStore } from 'stores/accounts'
import Account from 'src/interface/Account'

export default defineComponent({
  // name: 'PageName'
  data() {
    return {
      newAccount: {
        email: '',
        username: '',
        password: ''
      } as Account,
      confirmPassword: '',
      loading: false,
      emailError: ''
    }
  },

  methods: {
    async register() {
      try {
        this.loading = true;
        let status = await this.AccountStore.createAccount(this.newAccount);
        switch (status) {
          case 201:
            this.$q.notify({
              type: 'positive',
              message: "Registration Successful"
            });
            this.$router.push('/login')
            break;
          case 409:
            this.emailError = "Email address have been used"
            break;
          default:
            if (status >= 500) {
              this.$q.notify({
                type: 'negative',
                message: 'Server Error'
              });
            }
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

    }
  },
  computed: {
    ...mapStores(useAccountStore)
  }
})
</script>
<template>
  <main>
    <div style="min-height: 1rem;"></div>
    <div>
      <q-form id="form" @submit.prevent="register">
        <div class="header">
          <h>Sign Up</h>
          <P>Get House for the best price</P>
        </div>
        <div class="containers">
          <q-input :loading="loading" :error="!!emailError" :errorMessage="emailError" label="Email" type="email" id="email"
            :rules="[value => !!value || 'Email Required', value => /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/g.test(value) || 'Invalid Email']"
            autocomplete="username" v-model="newAccount.email" @change="emailError = ''" />
          <q-input :loading="loading"  label="Username" v-model="newAccount.username" :rules="[value => !!value || 'Username Required']" />
          <q-input :loading="loading"  label="Password" type="password" autocomplete="new-password"
            :rules="[value => !!value || 'Password Required']" v-model="newAccount.password" autoCom />
          <q-input :loading="loading"  label="Confirm Password" type="password" autocomplete="new-password"
            :rules="[value => !!value || 'Confirm Password Required', value => value === newAccount.password || 'Password mismatch']"
            v-model="confirmPassword" />
        </div>
        <div>
          <P>Already a member<router-link to="/login">Click here</router-link></P>
          <q-btn :loading="loading"  type="submit" color="primary">Sign Up</q-btn>
        </div>
      </q-form>
    </div>
    <div style="min-height: 1rem;"></div>
  </main>
</template>


<style scoped>
.header{
  font-size: 20px;
  font-weight: 200;
  font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;
}
.containers{
  width:0px;
}
main {
  display: grid;
  grid-template-rows: auto min-content auto;
  flex-direction: column;
  position: relative;
  flex: auto;
  background-image: url("assets/images/register1.jpg");
  background-size: 100% 100%;
  background-repeat: no-repeat;
  margin: 0;
}

main>div {
  display: flex;
  justify-content: center;
}

#form {
  padding: 1rem;
  background-color: white;
  border-radius: 2rem;
}

#form>div {
  margin: 2rem;
}

#form>div:first-child>h1 {
  text-align: center;
}

#form>div>p {
  color: blue;
}

#form>div>p>a {
  color: orange;
}


#form>div:nth-child(2) {
  display: grid;
  grid-template-columns: max-content;
}

#form>div:nth-child(2)>input {
  width: 100%;
}
</style>
