export class PurchaseOrder {
  id: number | undefined;
  purchaseOrderCode: number | undefined;
  supplierCode: number | undefined;
  supplierName: string | undefined;
  orderDate: string | undefined;
  receivedDate: string | undefined;
  productCode: number | undefined;
  productName: string | undefined;
  unitPrice: number | undefined;  
  purchaseQuantity: number | undefined;
  totalAmount: number | undefined;
  paymentStatus: string | undefined;
  status: string | undefined;
}
