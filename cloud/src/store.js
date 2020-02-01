import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate";

import axios from './axios'
import {AUTH_REQUEST, GET_USER, AUTH_LOGOUT, GET_ORGANISATIONS, ADD_ORGANISATION, GET_ORGANISATION, EDIT_ORGANISATION, GET_USERS, ADD_USER, EDIT_USER, DELETE_USER } from './actions'
import { AUTH_ERROR, AUTH_SUCCESS, ADD_ORG, SET_ORGANISATIONS, SET_ORGANISATION, SET_MODIFIED_ORG, SET_USERS, SET_USER, SET_MODIFIED_USER } from './mutations'

Vue.use(Vuex)

export default new Vuex.Store({
  plugins: [createPersistedState()],
  state : {
    user: null,
    status: null,
    organisations: [],
    organisation: null,
    users: [],
    selectedUser : null
  },
  getters : {
    isAuth: state => { 
      state.user = JSON.parse(localStorage.getItem('user'))
      return state.user !== null },
    role : state =>  { 
      state.user = JSON.parse(localStorage.getItem('user'))
      return state.user ? state.user.role :  ""
     },
    org: state => { 
      state.user = JSON.parse(localStorage.getItem('user'))
      return state.user ? state.user.org :  ""
    },
    orgs: state => state.organisations,
    selectedOrg: state => state.organisation,
    users: state => state.users,
    selectedUser: state => state.selectedUser
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
    [ADD_ORG] : (state, org) => {
      state.organisations = {...state.organisations, org}
    },
    [SET_ORGANISATIONS]: (state, orgs) => {
      state.organisations = orgs
    },
    [SET_ORGANISATION] : (state, org) => {
      state.organisation = org
    },
    [SET_MODIFIED_ORG] : (state, org) => {
      let newState = {}
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
      state.selectedUser = user
    },
    [SET_MODIFIED_USER] : (state, user) => {
      const index = state.users.findIndex(us => us.email === user.email)

      state.users = [...state.users.slice(0, index), user, ...state.users.slice(index + 1, state.users.length)]
    },
    [DELETE_USER] : (state, email) => {
      const index = state.users.findIndex(u => u.email == email)

      state.users = [...state.users(0, index), ...state.users(index + 1, state.users.length)]
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
          commit(ADD_ORG, res.data);
          resolve(res)
        })
        .catch(err => {
          reject(err)
        })
      })
    },
    [EDIT_ORGANISATION]: ({commit, dispatch}, data) => {
      const dataServer = { name: data.name, desc: data.description, logo_url: data.logo}
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
        axios(`/org/${data.email}`, {
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

  }
})