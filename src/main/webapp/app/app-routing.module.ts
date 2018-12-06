import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute, navbarRoute } from './layouts';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                ...LAYOUT_ROUTES,
                {
                    path: 'admin',
                    loadChildren: './admin/admin.module#BiodifulAdminModule'
                }
            ],
            // See issue described here if we don't use hashtag:
            // https://stackoverflow.com/questions/48567071/jhipster-refresh-url-cause-cannot-get-user-management?rq=1
            //{ useHash: false, enableTracing: DEBUG_INFO_ENABLED }
            { useHash: true, enableTracing: DEBUG_INFO_ENABLED }
        )
    ],
    exports: [RouterModule]
})
export class BiodifulAppRoutingModule {}
