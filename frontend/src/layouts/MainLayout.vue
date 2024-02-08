<template>
  <div>
    <div class="row">
      <q-toolbar class="col-9 q-py-sm">
        <q-toolbar-title class="clickable-title  q-ml-xl">
          <div @click="routeToMainWeb">MyCal</div>
        </q-toolbar-title>
        <q-space />

        <q-tabs v-model="tab" shrink>
          <q-route-tab label="Explore" to="/explore" exact />
          <q-route-tab label="Calculator" to="/calculator" exact />
          <q-route-tab label="Booked Properties" to="/myresaleprop" exact />
          <q-route-tab label="Manage Properties" to="/property/manage" exact />
        </q-tabs>
        <q-btn
          flat

          round
          dense
          icon="help_outline"
          size="lg"
          class="q-mr-sm"
          to="/helppg"
          style="color: #5e5e5e"
        />
      </q-toolbar>

      <q-toolbar class="col-3 text-white" style="background-color: rgb(3, 3, 94);">
        <div
              v-if="sessionsStore.isLogin"
              style="background-color: rgb(3, 3, 94); width: 100%; padding: 0 margin-left: auto;"
            >
              <q-btn-dropdown
                v-model="menu"
                flat
                stretch
                dense
                color="white"
                dropdown-icon="menu"
                style="height: 100%"
                size="md"
                content-style="background-color:rgb(3, 3, 94);; color: white"
                class="q-pa-sm"
              >
                <q-list>
                  <q-item clickable to="/savedProperty">
                    <q-item-section avatar>
                      <q-avatar
                        icon="auto_awesome"
                        text-color="yellow"
                      ></q-avatar>
                    </q-item-section>
                    <q-item-section>
                      <q-item-label> Saved Properties </q-item-label>
                    </q-item-section>
                  </q-item>
                  <q-item clickable to="/viewparticulars">
                    <q-item-section avatar>
                      <q-avatar
                        icon="account_box"
                        text-color="yellow"
                      ></q-avatar>
                    </q-item-section>
                    <q-item-section>
                      <q-item-label> My Particulars </q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
              </q-btn-dropdown>
              <span
                v-text="sessionsStore.sessionAccount?.username"
                style="color: white; font-size: 1rem"
                class = "q-pr-xl"
              >
            </span>
              <q-btn outline @click="confirm = true">Log Out</q-btn>
            </div>

            <div v-else class="row items-center">
                <q-btn outline class = "q-mr-xl q-ml-md" to="/registration">Register</q-btn>
                <q-btn color="primary" to="/login">Login</q-btn>
            </div>
        <q-space />
        <q-dialog v-model="confirm" persistent>
    <q-card>
      <q-card-section class="row items-center">
        <q-avatar icon="logout" color="red" text-color="white" />
        <span class="q-ml-sm">Are you sure you want to log out</span>
      </q-card-section>

      <q-card-actions>
        <q-btn label="no" color="primary" v-close-popup />
        <q-space />
        <q-btn label="Log Out" color="red" @click="logOut" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
      </q-toolbar>
    </div>
  </div>

  <router-view v-if="loaded" />
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { mapStores } from "pinia";
import { ref } from "vue";

import { useSessionsStore } from "stores/sessions";
import { useAccountStore } from "stores/accounts";

export default defineComponent({
  name: "MainLayout",

  components: {},

  data() {
    return {
      loaded: false,
      menu: false,
      confirm: false,
      tab: ref(""),
      userID:""
    };
  },

  methods: {
    logOut() {
      this.sessionsStore.logout();
      this.$router.replace("/");
    },
    routeToMainWeb() {
      this.$router.push("/");
    },
    returnUsername(){
      this.sessionsStore.sessionAccount?.username;
    }
  },
  computed: {
    ...mapStores(useSessionsStore),
  },
  async created() {
    try {
      if (this.sessionsStore.isLogin)
        await this.sessionsStore.getSessionAccount();
    } finally {
      this.loaded = true;
    }
  }
});
</script>
<style>
.clickable-title {
  cursor: pointer;
}
</style>


