<script lang="ts">
import { defineComponent } from 'vue'
import { mapStores } from 'pinia'

import { useSessionsStore } from 'stores/sessions'
import { bexBackground } from 'quasar/wrappers'

export default defineComponent({
  name: 'login',
  data() {
    return {
      email: '',
      password: '',
      loading: false,
      emailError: '',
      PasswordError: '',
      pwVisibility: false
    }
  },
  methods: {
    async login() {
      try {
        this.loading = true;
        let status = await this.sessionsStore.login(this.email, this.password);
        switch (status) {
          case 201:
            if (typeof this.$route.query.from === 'string') {
              this.$router.push(this.$route.query.from);
            } else {
              this.$router.push('/');
            }

            break;
          case 404:
            this.emailError = 'Wrong Email'
            break;
          case 401:
            this.PasswordError = 'Wrong Password'
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
    ...mapStores(useSessionsStore)
  }
})

</script>

<template>
  <main>
    <div style="min-height: 1rem;"></div>
    <div>
      <q-form id="form" @submit.prevent="login">
        <div>
          <div class = "text-h5 text-center">Sign in</div>
          <div class = "text-subtitle1 text-center q-mb-xl">Sign in below to access your account</div>

          <q-input :loading="loading" :error="!!emailError" :error-message="emailError" @change="emailError = ''"
            label="Email" type="email" id="email" autocomplete="username" v-model="email" outlined style="width: 100%;"/>
          <q-input :loading="loading" :error="!!PasswordError" :error-message="PasswordError" @change="PasswordError = ''"
            label="Password" :type="pwVisibility ? 'text' : 'password'" id="password" autocomplete="current-password"
            v-model="password" outlined style="width: 100%;" justify="center">
            <template v-slot:append>
              <q-icon name="visibility" @mousedown="pwVisibility = true" @mouseup="pwVisibility = false"
                style="cursor: pointer;" />
            </template>
          </q-input>

          <q-btn :loading="loading" size="md" style="width:100%; background:rgb(3, 3, 94); color: white;" type="submit" class="q-mt-lg q-mb-md">Sign in</q-btn>

          <P class = "text-center"><span>Don't have an account yet? </span><router-link to="/registration">Sign up</router-link></P>
        </div>
      </q-form>
    </div>
  </main>
</template>
<style scoped>
main {
  display: grid;
  grid-template-rows: auto min-content auto;
  flex-direction: column;
  position: relative;
  flex: auto;
  background-image: url("assets/images/Login_img.jpg");
  background-size: 100% 100%;
  background-repeat: no-repeat;
  margin: 0;
}

main>div {

  display: flex;
  justify-content: center;

}

#form {
  padding: 0.5rem;
  background-color: white;
  border-radius: 0.5rem;
  width: 30%
}

#form>div {
  margin: 1rem;
}

#form>div:first-child>h4 {
  text-align: center;
}

#form>div:first-child>p>a {
  color: blue;
}

#form>div:nth-child(2) {
  display: grid;
  grid-template-columns: max-content;
}

#form>div:nth-child(2)>input {
  justify-content: center;
  width: 100%;
}</style>

