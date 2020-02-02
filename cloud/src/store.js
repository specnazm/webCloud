import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate";

import axios from './axios'
import {
  AUTH_REQUEST, AUTH_LOGOUT,  
  GET_ORGANISATIONS, ADD_ORGANISATION, GET_ORGANISATION, EDIT_ORGANISATION, 
  GET_USERS, GET_USER,ADD_USER, EDIT_USER, DELETE_USER, EDIT_PROFILE, 
  GET_CATEGORIES, GET_CATEGORY, ADD_CATEGORY, EDIT_CATEGORY, DELETE_CATEGORY, 
  GET_DISKS, GET_DISK, ADD_DISK, EDIT_DISK, DELETE_DISK } from './actions'
import { 
   AUTH_ERROR, AUTH_SUCCESS, 
   SET_ORGANISATIONS, SET_ORGANISATION, SET_MODIFIED_ORG, 
   SET_USERS, SET_USER, SET_MODIFIED_USER,
   SET_CATEGORIES, SET_CATEGORY, SET_MODIFIED_CATEGORY,
   SET_DISKS, SET_DISK, SET_MODIFIED_DISK } from './mutations'

Vue.use(Vuex)

export default new Vuex.Store({
  plugins: [createPersistedState()],
  state : {
    user: null,
    status: null,

    organisations: [],
    selectedOrg: null,

    users: [],
    selectedUser : null,

    categories : [],
    selectedCat: null,

    disks: [],
    selectedDisk: null
  },
  getters : {
    isAuth: state => { 
      state.user = JSON.parse(localStorage.getItem('user'))
      return state.user !== null 
    },
    loggedUser: state =>  JSON.parse(localStorage.getItem('user')),
    role : state =>  { 
      state.user = JSON.parse(localStorage.getItem('user'))
      return state.user ? state.user.role :  ""
     },
    org: state => { 
      state.user = JSON.parse(localStorage.getItem('user'))
      return state.user ? state.user.org :  ""
    },

    orgs: state => state.organisations,
    selectedOrg: state => state.selectedOrg,

    users: state => state.users,
    selectedUser: state => state.selectedUser,

    categories: state => state.categories

  },
  mutations: {
    [AUTH_REQUEST]: (state) => {
      state.status = 'loading'
    },
    [AUTH_SUCCESS]: (state, user) => {
      state.status = 'success'
      state.user = user
    },
    [AUTH_ERROR]: (state) => {
      state.status = 'error'
    },
    [AUTH_LOGOUT]: (state) => {
      state.user = null
    },


    [ADD_ORGANISATION] : (state, org) => {
      state.organisations = {...state.organisations, org}
    },
    [SET_ORGANISATIONS]: (state, orgs) => {
      state.organisations = orgs
    },
    [SET_ORGANISATION] : (state, org) => {
      state.selectedOrg = org
    },
    [SET_MODIFIED_ORG] : (state, org) => {
      let newState = {}
      console.log('u state', org)
      for (const orgName in Object.keys(state.organisations)) {
        if (orgName === org.Name) {
            newState[orgName] = org
        } else
          newState[orgName] = state.organisations[orgName]
    }
      state.organisations = newState;
    },


    [SET_USERS] : (state, users) => {
      state.users = users
    },
    [ADD_USER] : (state, user) => {
      state.users = [...state.users, user]
    },
    [SET_USER] : (state, user) => {
      state.selectedUser = {...user}
    },
    [SET_MODIFIED_USER] : (state, user) => {
      const index = state.users.findIndex(us => us.email === user.email)

      state.users = [...state.users.slice(0, index), user, ...state.users.slice(index + 1, state.users.length)]
    },
    [DELETE_USER] : (state, email) => {
      const index = state.users.findIndex(u => u.email == email)

      state.users = [...state.users.slice(0, index), ...state.users.slice(index + 1, state.users.length)]
    },


    [SET_CATEGORIES] : (state, categories) => {
      state.categories = categories
    },
    [ADD_CATEGORY] : (state, cat) => {
      state.categories = [...state.categories, cat]
    },
    [SET_CATEGORY] : (state, cat) => {
      state.selectedCat = {...cat}
    },
    [SET_MODIFIED_CATEGORY] : (state, cat) => {
      const index = state.categories.findIndex(c => c.name === cat.name)

      state.categories = [...state.categories.slice(0, index), cat, ...state.categories.slice(index + 1, state.categories.length)]
    },
    [DELETE_CATEGORY] : (state, name) => {
      const index = state.categories.findIndex(c => c.name == name)
      
      state.categories = [...state.categories.slice(0, index), ...state.categories.slice(index + 1, state.categories.length)]
    },

    
    [SET_DISKS] : (state, disks) => {
      state.disks = disks
    },
    [ADD_DISK] : (state, disk) => {
      state.disks = [...state.disks, disk]
    },
    [SET_DISK] : (state, disk) => {
      state.selectedDisk = {...disk}
    },
    [SET_MODIFIED_DISK] : (state, disk) => {
      const index = state.disks.findIndex(d => d.name === disk.name)

      state.disks = [...state.disks.slice(0, index), disk, ...state.disks.slice(index + 1, state.disks.length)]
    },
    [DELETE_DISK] : (state, name) => {
      const index = state.disks.findIndex(d => d.name == name)
      
      state.disks = [...state.disks.slice(0, index), ...state.disks.slice(index + 1, state.disks.length)]
    }
  },
  actions: {
    [AUTH_REQUEST]: ({commit, dispatch}, data) => {
      return new Promise((resolve, reject) => { 
        commit(AUTH_REQUEST)
        axios('/auth/login', {
          method: 'POST',
          data: JSON.stringify(data)
        })
        .then(res => {
          commit(AUTH_SUCCESS, res.data);
          localStorage.setItem('user', JSON.stringify(res.data));
          resolve(res)
        })
        .catch(err => {
          commit(AUTH_ERROR)
          reject(err)
        })
      })
    },
    [AUTH_LOGOUT]: ({commit, dispatch}) => {
      return new Promise((resolve, reject) => {
        axios.get('/auth/logout') 
        .then(res => {
          commit(AUTH_LOGOUT)
          localStorage.removeItem('user') 
          resolve()
        })
        .catch(err => {
          commit(AUTH_ERROR)
          reject(err)
        })
      })
    },


    [GET_ORGANISATIONS]: ({commit, dispatch}) => {
      return new Promise((resolve, reject) => { 
        axios.get('/org')
        .then(res => {
          commit(SET_ORGANISATIONS, res.data);
          resolve(res)
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [ADD_ORGANISATION]: ({commit, dispatch}, data) => {
      return new Promise((resolve, reject) => { 
        axios('/org', {
          method: 'POST',
          data: JSON.stringify(data),
          headers: {
            'Content-Type': 'multipart/form-data'
        }
        })
        .then(res => {
          commit(ADD_ORGANISATION, res.data);
          resolve(res)
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [EDIT_ORGANISATION]: ({commit, dispatch}, data) => {
      const dataServer = { name: data.name, desc: data.desc, logo_url: data.logo}
      return new Promise((resolve, reject) => { 
        axios(`/org/${data.oldName}`, {
          method: 'PUT',
          data: JSON.stringify(dataServer),
          headers: {
            'Content-Type': 'multipart/form-data'
        }
        })
        .then(res => {
          commit(SET_MODIFIED_ORG, res.data);
          resolve()
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [GET_ORGANISATION]: ({commit, dispatch}, { name } ) => {
      return new Promise((resolve, reject) => { 
        axios.get(`/org/${name}`)
        .then(res => {
          commit(SET_ORGANISATION, res.data);
          resolve(res)
        })
        .catch(err => {
          reject(err)
        })
      })
    },


    [GET_USERS]: ({commit, dispatch}) => {
      return new Promise((resolve, reject) => { 
        axios.get('/user')
        .then(res => {
          commit(SET_USERS, res.data);
          resolve(res)
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [ADD_USER]: ({commit, dispatch}, data) => {
      return new Promise((resolve, reject) => { 
        axios.post('/user', JSON.stringify(data))
        .then(res => {
          commit(ADD_USER, res.data);
          resolve(res)
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [GET_USER]: ({commit, dispatch}, { email } ) => {
      return new Promise((resolve, reject) => { 
        axios.get(`/user/${email}`)
        .then(res => {
          commit(SET_USER, res.data);
          resolve(res)
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [EDIT_USER]: ({commit, dispatch}, data) => {
 
      return new Promise((resolve, reject) => { 
        axios(`/user/${data.email}`, {
          method: 'PUT',
          data: JSON.stringify(data)
        })
        .then(res => {
          commit(SET_MODIFIED_USER, res.data);
          resolve()
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [DELETE_USER]: ({commit, dispatch}, id) => {
 
      return new Promise((resolve, reject) => { 
        axios(`/user/${id}`, {
          method: 'DELETE'
        })
        .then(res => {
          commit(DELETE_USER, id);
          resolve()
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [EDIT_PROFILE]: ({commit, dispatch}, data) => {
 
      return new Promise((resolve, reject) => { 
        axios(`/me`, {
          method: 'PUT',
          data: JSON.stringify(data)
        })
        .then(res => {
          commit(SET_MODIFIED_USER, res.data)
          commit(AUTH_SUCCESS, res.data)
          localStorage.setItem('user', JSON.stringify(res.data));
          resolve()
        })
        .catch(err => {
          reject(err)
        })
      })
    },


    [GET_CATEGORIES]: ({commit, dispatch}) => {
      return new Promise((resolve, reject) => { 
        axios.get('/category')
        .then(res => {
          commit(SET_CATEGORIES, res.data);
          resolve(res)
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [ADD_CATEGORY]: ({commit, dispatch}, data) => {
      return new Promise((resolve, reject) => { 
        axios.post('/category', JSON.stringify(data))
        .then(res => {
          commit(ADD_CATEGORY, res.data);
          resolve(res)
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [GET_CATEGORY]: ({commit, dispatch}, { name } ) => {
      return new Promise((resolve, reject) => { 
        axios.get(`/category/${name}`)
        .then(res => {
          commit(SET_CATEGORY, res.data);
          resolve(res)
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [EDIT_CATEGORY]: ({commit, dispatch}, data) => {
 
      return new Promise((resolve, reject) => { 
        axios(`/category/${data.name}`, {
          method: 'PUT',
          data: JSON.stringify(data)
        })
        .then(res => {
          commit(SET_MODIFIED_CATEGORY, res.data);
          resolve()
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [DELETE_CATEGORY]: ({commit, dispatch}, id) => {
 
      return new Promise((resolve, reject) => { 
        axios(`/category/${id}`, {
          method: 'DELETE'
        })
        .then(res => {
          commit(DELETE_CATEGORY, id);
          resolve()
        })
        .catch(err => {
          reject(err)
        })
      })
    },


    [GET_DISKS]: ({commit, dispatch}) => {
      return new Promise((resolve, reject) => { 
        axios.get('/disk')
        .then(res => {
          commit(SET_DISKS, res.data);
          resolve(res)
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [ADD_DISK]: ({commit, dispatch}, data) => {
      return new Promise((resolve, reject) => { 
        axios.post('/disk', JSON.stringify(data))
        .then(res => {
          commit(ADD_DISK, res.data);
          resolve(res)
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [GET_DISK]: ({commit, dispatch}, { name } ) => {
      return new Promise((resolve, reject) => { 
        axios.get(`/disk/${name}`)
        .then(res => {
          commit(SET_DISK, res.data);
          resolve(res)
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [EDIT_DISK]: ({commit, dispatch}, data) => {
 
      return new Promise((resolve, reject) => { 
        axios(`/disk/${data.name}`, {
          method: 'PUT',
          data: JSON.stringify(data)
        })
        .then(res => {
          commit(SET_MODIFIED_CATEGORY, res.data);
          resolve()
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [DELETE_DISK]: ({commit, dispatch}, id) => {
 
      return new Promise((resolve, reject) => { 
        axios(`/category/${id}`, {
          method: 'DELETE'
        })
        .then(res => {
          commit(DELETE_DISK, id);
          resolve()
        })
        .catch(err => {
          reject(err)
        })
      })
    },
  }
})