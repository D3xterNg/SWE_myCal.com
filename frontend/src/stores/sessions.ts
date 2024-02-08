import { defineStore } from 'pinia';
import { LocalStorage } from 'quasar'

import Account from 'src/interface/Account';

export const useSessionsStore = defineStore('sessions', {
  state: () => ({
    sessionAccount: null as Account | null,
    isLogin: LocalStorage.getItem('isLogin') as boolean ?? false,
    userId: null as string | null
  }),

  getters: {

  },

  actions: {
    async login(email: string, password: string): Promise<number> {
      let res = await fetch('/api/v1/sessions', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email, password: password })
      });
      if (res.status === 201) {
        this.isLogin = true;
        let returnId = await res.text();
        returnId = returnId.trim();
        this.userId = returnId;
        LocalStorage.set('isLogin', true);
        LocalStorage.set('userId', returnId);
        console.log(res.status)
        await this.getSessionAccount();
      }
      return res.status;
    },
    async logout() {
      await fetch('/api/v1/sessions', {method : 'DELETE'});
      this.isLogin = false;
      LocalStorage.set('isLogin', false);
      this.sessionAccount = null;
    },
    async getSessionAccount(): Promise<Account | null> {
      let res = await fetch('/api/v1/sessions/account');
      if (res.status !== 200) {
        this.isLogin = false;
        LocalStorage.set('isLogin', false);
        return null
      };
      this.sessionAccount = await res.json();
      return this.sessionAccount;
    },
    async getUserId() {
      return this.userId;
    }

  }
});
