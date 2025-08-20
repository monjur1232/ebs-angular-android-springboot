export class Inventory {
  id: number | undefined;
  productCode: number | undefined;
  productName: string | undefined;
  availableQuantity: number = 0;
  purchasedQuantity: number = 0;
  soldQuantity: number = 0;
  returnedToSupplier: number = 0;
  returnedFromCustomer: number = 0;
  reorderLevel!: number;
}
