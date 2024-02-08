import { RouteRecordRaw } from 'vue-router';
const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '/', component: () => import('pages/IndexPage.vue') },
      { path:'login', component: () => import('pages/LoginPage.vue') },
      { path: 'registration' , component: () => import('pages/Registration.vue') },
      { path: 'booking' , component: () => import('pages/Booking.vue') },
      { path: 'property/create' , component: () => import('pages/CreateProperty.vue') },
      { path: 'property/info', component: () => import('pages/PropertyInfo.vue') },
      { path: 'property/manage', component: () => import('pages/ManageProperties.vue') },
      { path: 'savedProperty', component: () => import('pages/savedProperty.vue') },
      { path: 'property/manage', component: () => import('pages/ManageProperties.vue')  },
      { path: 'property/edit', component: () => import('pages/EditProperty.vue') },
      { path: 'explore', component: () => import('pages/ExplorePage.vue') },
      { path: 'calculator', component: () => import('pages/ResaleCalculator.vue') },
      { path: '/myresaleprop', component: () => import('pages/MyResaleProperties.vue') },
      { path: '/helppg', component: () => import('pages/HelpPage.vue') },
      { path: '/updateparticulars', component: () => import('pages/UpdateParticulars.vue') },
      { path: '/viewparticulars', component: () => import('pages/ViewParticulars.vue') },
      { path: '/searchrental', component: () => import('pages/SearchRentalProperty.vue') },
    ],
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/403',
    component: () => import('pages/ErrorForbidden.vue')
  },
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
