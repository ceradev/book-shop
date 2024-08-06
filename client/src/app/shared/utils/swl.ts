import { Injectable } from "@angular/core";
import Swal, { SweetAlertResult } from "sweetalert2";

const Toast = Swal.mixin({
  toast: true,
  position: "top-end",
  showConfirmButton: false,
  timer: 2000,
  timerProgressBar: true,
  didOpen: (toast) => {
    toast.onmouseenter = Swal.stopTimer;
    toast.onmouseleave = Swal.resumeTimer;
  }
});

@Injectable({
  providedIn: 'root',
})
export class SwlAlerts{
 alertError(message: string) :void {
    Swal.fire({
      title: '¡ERROR!',
      text: message,
      icon: 'error',
      confirmButtonColor:  "#DD7569",
      confirmButtonText: 'Cerrar',
    });
  }
  alertSuccess(message: string){
    Swal.fire({
      title: '¡EXITO!',
      text: message,
      icon: 'success',
      confirmButtonColor:  "#b8987a",
      confirmButtonText: 'Cerrar',
    });
  }
  showLoandingModal(message: string) {
    Swal.fire({
      title: message,
      allowOutsideClick: false,
      allowEscapeKey: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });
  }

  alertConfirmation(title: string, message: string): Promise<SweetAlertResult<any>> {
    return Swal.fire({
      title: title,
      text: message,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor:  "#b8987a",
      cancelButtonColor:"#DD7569",
      confirmButtonText: "Confirmar",
      cancelButtonText: "Cancelar",
    });
  }
  alertConfirmationWithDeny(title: string, message: string): Promise<SweetAlertResult<any>> {
    return Swal.fire({
      title: title,
      text: message,
      icon: "warning",
      showDenyButton: true,
      showCancelButton: true,
      confirmButtonText: "Iniciar Sesión",
      denyButtonText: "Registrarse",
      cancelButtonText: "Salir",
      confirmButtonColor:  "#876445",
      denyButtonColor: "#b8987a",
      cancelButtonColor:"#DD7569",
    });
  }

  showToastSuccess(message: string) {
    Toast.fire({
      icon: "success",
      title: message
    });
  }

  showToastError(message: string) {
    Toast.fire({
      icon: "error",
      title: message
    });
  }
}


