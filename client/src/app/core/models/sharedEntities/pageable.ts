export class Pageable {

    constructor(
        public pageNumber: number,
        public pageSize: number,
        public sorts: any[] | null
      ) {}
      
}
