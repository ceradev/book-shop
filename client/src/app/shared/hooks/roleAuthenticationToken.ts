import { jwtDecode } from 'jwt-decode';

export function roleAuthenticationToken(token: string) {
  const decodedToken: any = jwtDecode(token);
  const role = decodedToken.resource_access.backend.roles[0];
  
  switch (role) {
    case 'admin_client_role':

      return 'admin';

    case 'seller_client_role':

      return 'seller';

    case 'client_client_role':
      console.log('client');
      return 'client';

    default:

      return null;
  }
}
