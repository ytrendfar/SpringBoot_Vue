import VueRouter from "vue-router"
import Manage from "@/pages/Manage"
import About from "@/pages/About"
import User from "@/pages/User"
import Home from "@/pages/Home"
import store from "@/store";
import Login from "@/pages/Login";
import Register from "@/pages/Register";
import File from "@/pages/File";
import Mine from "@/pages/Mine";
import Role from "@/pages/Role";
import Menus from "@/pages/Menus";

const router = new VueRouter({
    routes: [
        {
            path: '/',
            name: 'Manage',
            component: Manage,
            redirect: '/home',
            children: [
                {
                    path: 'user',
                    name: 'User',
                    component: User,
                    meta: {pathName: '用户管理'}
                },
                {
                    path: 'home',
                    name: 'Home',
                    component: Home,
                    meta: {pathName: '首页'}
                },
                {
                    path: 'file',
                    name: 'File',
                    component: File,
                    meta: {pathName: '文件管理'}
                },
                {
                    path: 'mine',
                    name: 'Mine',
                    component: Mine,
                    meta: {pathName: '个人中心'}
                },
                {
                    path: 'role',
                    name: 'Role',
                    component: Role,
                    meta: {pathName: '角色管理'}
                },
                {
                    path: 'menus',
                    name: 'Menus',
                    component: Menus,
                    meta: {pathName: '菜单管理'}
                },

            ]
        },
        {
            path: '/about',
            name: 'About',
            component: About
        },
        {
            path: '/login',
            name: 'Login',
            component: Login
        },
        {
            path: '/register',
            name: 'Register',
            component: Register
        }
    ],
    mode: 'history'
})

router.beforeEach((to,from,next)=>{
    store.state.currentPath = to.meta.pathName
    next()
})

export default router