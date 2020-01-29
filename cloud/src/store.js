import Vue from 'vue'
import Vuex from 'vuex'

import axios from './axios'
import {AUTH_REQUEST, AUTH_SUCCESS, AUTH_ERROR, AUTH_LOGOUT} from './mutationTypes'

Vue.use(Vuex)

export default new Vuex.Store({
  state : {
    user: null,
    status: null
  },
  getters : {
    isAuth: state => state.user !== null
  },
  mutations: {
    [AUTH_REQUEST]: (state) => {
      state.status = 'loading'
    },
    [AUTH_SUCCESS]: (state, user) => {
      state.status = 'success';
      state.user = user;
    },
    [AUTH_ERROR]: (state) => {
      state.status = 'error'
    },
    [AUTH_LOGOUT]: (state) => {
      state.user = null;
    }
  },
  actions: {
    [AUTH_REQUEST]: ({commit, dispatch}, data) => {
      return new Promise((resolve, reject) => { 
        commit(AUTH_REQUEST)
        axios.post('/auth/login', data)
        .then(res => {
          commit(AUTH_SUCCESS, res.data);
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
        commit(AUTH_LOGOUT)
        resolve()
      })
    }
  }
})