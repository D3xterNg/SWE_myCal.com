import {defineStore} from 'pinia';

import PersonalParticular from 'src/interface/PersonalParticular'

export const usePersonalParticularStore = defineStore('personalParticular', {
  state: () => ({
    counter: 0
  }),

  getters: {

  },

  actions: {
    async getPersonalParticularByUserId(userId: string): Promise<PersonalParticular> {
      let res = await fetch(`/api/v1/accounts/${userId}/viewPersonalParticular`);
      return await res.json();
    },

    async createPersonalParticular(personalParticular: PersonalParticular): Promise<string> {
      let res = await fetch(`/api/v1/personal-particulars`, {
        method: 'POST',
        body: JSON.stringify(personalParticular),
        headers: {
          'Content-Type': 'application/json'
        }
      });
      return await res.text();
    },

    // async updatePersonalParticular(userId: string, personalParticular: PersonalParticular): Promise<number> {
    //   console.log("updatePersonalParticular");
    //   let res = await fetch(`/api/v1/${userId}/updatePersonalParticular`, {
    //     method: 'POST',
    //     body: JSON.stringify(personalParticular),
    //     headers: {
    //       'Content-Type': 'application/json'
    //     }
    //   });
    //   return res.status;
    // },
    // async getPersonalParticularByRecentlyAdded(): Promise<PersonalParticular[]> {
    //   let res = await fetch(`/api/v1/personal-particulars`);
    //   return await res.json();
    // },
    async deletePersonalParticular(id: string): Promise<number> {
      let res = await fetch('/api/v1/personal-particulars/' + id , {method: "DELETE"});
      return res.status;

    },
    async getPersonalParticular(id: string): Promise<PersonalParticular> {
      let res = await fetch('/api/v1/personal-particulars/' + id ,);
      return await res.json();

    }
  }
});
