import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environment/environment';
import { Address } from '@models/Address';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AddressService {
  constructor(private http: HttpClient) {}

  getAllAddressesByUserId(userId: string): Observable<Address[]> {
    return this.http.get<Address[]>(
      environment.urlApi + '/addresses/' + userId
    );
  }

  getYourAddresses(): Observable<Address[]> {
    return this.http.get<Address[]>(environment.urlApi + '/addresses/me');
  }

  addNewAddress(address: Address) {
    return this.http.post(environment.urlApi + '/addresses', address);
  }

  deleteAddress(id: number) {
    return this.http.delete(environment.urlApi + '/addresses/' + id);
  }

  updateAddress(address: Address) {
    return this.http.put(environment.urlApi + '/addresses/', address);
  }
}
