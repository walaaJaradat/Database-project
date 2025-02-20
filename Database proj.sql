DROP DATABASE IF EXISTS project;
CREATE DATABASE project;
USE project;

CREATE TABLE Customer (
    CustomerID INTEGER AUTO_INCREMENT PRIMARY KEY,
    CustomerName VARCHAR(50) NOT NULL,
    CustomerAddress VARCHAR(100)
);

CREATE TABLE Customer_Email (
    EmailID INTEGER AUTO_INCREMENT PRIMARY KEY,
    CustomerID INTEGER,
    CustomerEmail VARCHAR(50),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID) ON DELETE CASCADE
);

CREATE TABLE Customer_Number (
    PhoneID INTEGER AUTO_INCREMENT PRIMARY KEY,
    CustomerID INTEGER,
    CustomerNumber BIGINT,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID) ON DELETE CASCADE
);

CREATE TABLE Product (
    Product_ID INTEGER AUTO_INCREMENT PRIMARY KEY,
    Product_Name VARCHAR(80) NOT NULL,
    Description TEXT,
    Category VARCHAR(50),
    Price DECIMAL(10, 2) NOT NULL CHECK (Price >= 0),
    In_Out_Stock VARCHAR(10) CHECK (In_Out_Stock IN ('IN', 'OUT'))
);

CREATE TABLE Product_Details (
    DetailID INTEGER AUTO_INCREMENT PRIMARY KEY,
    Product_ID INTEGER,
    Color VARCHAR(50),
    Size VARCHAR(50),
    Quantity INTEGER NOT NULL CHECK (Quantity >= 0),
    FOREIGN KEY (Product_ID) REFERENCES Product(Product_ID) ON DELETE CASCADE
);

CREATE TABLE Discount (
    DiscountID INTEGER AUTO_INCREMENT PRIMARY KEY,
    DiscountPercentage FLOAT NOT NULL CHECK (DiscountPercentage >= 0 AND DiscountPercentage <= 100),
    StartDate DATETIME NOT NULL,
	EndDate DATETIME NOT NULL
);


CREATE TABLE Orders (
    Order_ID INTEGER AUTO_INCREMENT PRIMARY KEY,
    Customer_ID INTEGER NOT NULL,
    Price DECIMAL(10, 2) NOT NULL CHECK (Price >= 0),
    Order_Date DATE NOT NULL,
    DiscountID INTEGER,
    OriginalPrice DECIMAL(10, 2) NOT NULL CHECK (OriginalPrice >= 0),
    DiscountRate FLOAT  CHECK (DiscountRate >= 0 AND DiscountRate <= 100),
    CostPrice DECIMAL(10, 2) NOT NULL CHECK (CostPrice >= 0),
    FOREIGN KEY (DiscountID) REFERENCES Discount(DiscountID) ON DELETE SET NULL,
    FOREIGN KEY (Customer_ID) REFERENCES Customer(CustomerID) ON DELETE CASCADE
);

CREATE TABLE Orders_Product (
    OP_Id INTEGER AUTO_INCREMENT PRIMARY KEY,
    Product_ID INTEGER NOT NULL,
    Order_ID INTEGER NOT NULL,
    Quantity INTEGER NOT NULL CHECK (Quantity > 0),
    FOREIGN KEY (Product_ID) REFERENCES Product(Product_ID) ON DELETE CASCADE,
    FOREIGN KEY (Order_ID) REFERENCES Orders(Order_ID) ON DELETE CASCADE
);

CREATE TABLE Suppliers (
    Suppliers_ID INTEGER AUTO_INCREMENT PRIMARY KEY,
    SuppliersName VARCHAR(50) NOT NULL
);

CREATE TABLE Supplier_Email (
    EmailID INTEGER AUTO_INCREMENT PRIMARY KEY,
    Suppliers_ID INTEGER,
    SuppliersEmail VARCHAR(100),
    FOREIGN KEY (Suppliers_ID) REFERENCES Suppliers(Suppliers_ID) ON DELETE CASCADE
);

CREATE TABLE Supplier_Number (
    PhoneID INTEGER AUTO_INCREMENT PRIMARY KEY,
    Suppliers_ID INTEGER,
    SupplierNumber BIGINT,
    FOREIGN KEY (Suppliers_ID) REFERENCES Suppliers(Suppliers_ID) ON DELETE CASCADE
);

CREATE TABLE Employee (
    Employee_Id INTEGER AUTO_INCREMENT PRIMARY KEY,
    EmployeeName VARCHAR(50) NOT NULL,
    Password CHAR(60) NOT NULL,
    Salary DECIMAL(10, 2) CHECK (Salary >= 0),
    Status VARCHAR(10) CHECK (Status IN ('manager', 'employee')),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Employee_Email (
    EmailID INTEGER AUTO_INCREMENT PRIMARY KEY,
    Employee_Id INTEGER,
    EmployeeEmail VARCHAR(50),
    FOREIGN KEY (Employee_Id) REFERENCES Employee(Employee_Id) ON DELETE CASCADE
);

CREATE TABLE Employee_Number (
    PhoneID INTEGER AUTO_INCREMENT PRIMARY KEY not null,
    Employee_Id INTEGER,
    EmployeeNumber BIGINT,
    FOREIGN KEY (Employee_Id) REFERENCES Employee(Employee_Id) ON DELETE CASCADE
);

CREATE TABLE Emp_Supp_Prod (
    EAP_ID INTEGER AUTO_INCREMENT PRIMARY KEY,
    Product_ID INTEGER NOT NULL,
    Employee_ID INTEGER NOT NULL,
    Suppliers_ID INTEGER NOT NULL,
    FOREIGN KEY (Product_ID) REFERENCES Product(Product_ID) ON DELETE CASCADE,
    FOREIGN KEY (Employee_ID) REFERENCES Employee(Employee_Id) ON DELETE CASCADE,
    FOREIGN KEY (Suppliers_ID) REFERENCES Suppliers(Suppliers_ID) ON DELETE CASCADE
);

INSERT INTO Customer (CustomerName, CustomerAddress) 
VALUES 
('Mohammad ahmad ', 'Jenin'),
('laila khalil', 'Nabuls'),
('khalid saeed', 'Hebron'),
('sumaia nasser ', 'Ramallah'),
('jamal abdallah', 'jerusalem'),
('Fatema mustafa', 'Hebron'),
('Ahmad ali', 'jenin'),
('Nadia hassan', 'jerusalem'),
('Yousef mahmmoud', 'yaffa'),
('Muna shareef', 'jerusalem');


INSERT INTO Customer_Email (CustomerID, CustomerEmail) 
VALUES 
(1, 'Mohammad.ahmad@gmail.com'),
(2, 'laila.khalil@gmail.com'),
(3, 'khalid.saeed@gmail.com'),
(4, 'sumaia.nasser@gmail.com'),
(5, 'jamal.abdallah@gmail.com'),
(6, 'Fatema.mustafa@gmail.com'),
(7, 'Ahmad.ali@gmail.com'),
(8, 'Nadia.hassan@gmail.com'),
(9, 'Yousef.mahmmoud@gmail.com'),
(10, 'Muna.shareef@gmail.com');



INSERT INTO Customer_Number (CustomerID, CustomerNumber) 
VALUES 
(1, 1234567890),
(2, 9876543210),
(3, 5556667777),
(4, 6666543210),
(5, 3456543333),
(6, 1176543290),
(7, 4576543299),
(8, 6776543221),
(9, 9076543666),
(10, 8976543555);


INSERT INTO Product (Product_Name, Description, Category, Price, In_Out_Stock) 
VALUES 
('T-shirt', 'High-neck', 'Men', 1500.00, 'IN'),
('Jeans', 'Slim-fit jeans', 'Men', 2000.00, 'IN'),
('Dress', 'Summer floral dress', 'Women', 2500.00, 'IN'),
('Sneakers', 'Running shoes', 'Men', 1800.00, 'OUT'),
('Jacket', 'Leather jacket', 'Men', 3500.00, 'IN'),
('Skirt', 'Pleated skirt', 'Women', 1200.00, 'IN'),
('Hoodie', 'Cotton hoodie', 'Kids', 1000.00, 'IN'),
('Shorts', 'Denim shorts', 'Kids', 800.00, 'OUT'),
('Blouse', 'Silk blouse', 'Women', 2200.00, 'IN'),
('Sweater', 'Woolen sweater', 'Kids', 1500.00, 'IN'),
('Coat', 'Winter coat', 'Women', 4000.00, 'IN'),
('Polo Shirt', 'Cotton polo shirt', 'Men', 1300.00, 'IN'),
('Leggings', 'Yoga leggings', 'Women', 900.00, 'OUT'),
('Cap', 'Baseball cap', 'Kids', 500.00, 'IN'),
('Socks', 'Ankle socks', 'Men', 300.00, 'IN'),
('Scarf', 'Woolen scarf', 'Women', 700.00, 'IN'),
('Gloves', 'Winter gloves', 'Kids', 600.00, 'IN'),
('Belt', 'Leather belt', 'Men', 1000.00, 'IN');

INSERT INTO Product_Details (Product_ID, Color, Size, Quantity)
VALUES
(1, 'Black', 'M', 50),
(1, 'White', 'L', 30),

(2, 'Blue', '32', 20),
(2, 'Black', '34', 15),

(3, 'Red', 'S', 10),
(3, 'Yellow', 'M', 8),

(4, 'Black', '42', 0),
(4, 'White', '44', 0),

(5, 'Brown', 'L', 12),
(5, 'Black', 'XL', 7),

(6, 'Black', 'S', 15),
(6, 'Blue', 'M', 10),

(7, 'Gray', 'S', 20),
(7, 'Black', 'M', 15),

(8, 'Blue', 'S', 0),
(8, 'Black', 'M', 0),

(9, 'White', 'S', 12),
(9, 'Pink', 'M', 10),

(10, 'Red', 'S', 15),
(10, 'Blue', 'M', 10),

(11, 'Black', 'L', 8),
(11, 'Gray', 'XL', 5),

(12, 'Blue', 'M', 20),
(12, 'White', 'L', 15),

(13, 'Black', 'S', 0),
(13, 'Gray', 'M', 0),

(14, 'Black', 'One Size', 30),
(14, 'Blue', 'One Size', 25),

(15, 'White', 'One Size', 50),
(15, 'Black', 'One Size', 40),

(16, 'Red', 'One Size', 20),
(16, 'Blue', 'One Size', 15),

(17, 'Black', 'One Size', 25),
(17, 'Gray', 'One Size', 20),

(18, 'Brown', 'One Size', 30),
(18, 'Black', 'One Size', 25);
INSERT INTO Discount (DiscountPercentage, StartDate, EndDate)
VALUES
(5.0, '2024-06-01 00:00:00', '2024-06-10 00:00:00'),
(10.0, '2024-06-02 00:00:00', '2024-06-11 00:00:00'),
(15.0, '2024-06-03 00:00:00', '2024-06-12 00:00:00'),
(20.0, '2024-06-04 00:00:00', '2024-06-13 00:00:00'),
(25.0, '2024-06-05 00:00:00', '2024-06-14 00:00:00'),
(30.0, '2024-06-06 00:00:00', '2024-06-15 00:00:00'),
(35.0, '2024-06-07 00:00:00', '2024-06-16 00:00:00'),
(40.0, '2024-06-08 00:00:00', '2024-06-17 00:00:00'),
(50.0, '2024-06-09 00:00:00', '2024-06-18 00:00:00');
INSERT INTO Orders (Customer_ID, Price, Order_Date, DiscountID, OriginalPrice, DiscountRate, CostPrice) 
VALUES 
(1, 1350.00, '2024-06-01', 2, 1500.00, 10.0, 1200.00),
(2, 640.00, '2024-06-02', 4, 800.00, 20.0, 600.00),
(3, 255.00, '2024-07-01', 3, 300.00, 15.0, 200.00),
(4, 500.00, '2024-07-15', NULL, 500.00, NULL, 400.00),
(5, 750.00, '2024-08-01', NULL, 750.00, NULL, 600.00),
(6, 500.00, '2024-08-02', NULL, 500.00, NULL, 400.00),
(7, 425.00, '2024-08-15', 3, 500.00, 15.0, 400.00),
(8, 300.00, '2024-07-20', 8, 500.00, 40.0, 250.00),
(9, 475.00, '2024-09-15', 1, 500.00, 5.0, 200.00),
(10, 500.00, '2024-07-20', NULL, 500.00, NULL, 300.00),
(1, 1400.00, '2024-08-05', 2, 1550.00, 10.0, 1300.00),
(2, 680.00, '2024-08-10', 4, 850.00, 20.0, 650.00),
(3, 270.00, '2024-08-15', 3, 320.00, 15.0, 230.00),
(4, 520.00, '2024-08-20', NULL, 520.00, NULL, 420.00),
(5, 780.00, '2024-08-25', NULL, 780.00, NULL, 630.00),
(6, 520.00, '2024-09-01', NULL, 520.00, NULL, 430.00),
(7, 440.00, '2024-09-05', 3, 500.00, 15.0, 410.00),
(8, 320.00, '2024-09-10', 8, 520.00, 40.0, 260.00),
(9, 490.00, '2024-09-15', 1, 400.00, 5.0, 210.00),
(10, 530.00, '2024-09-20', NULL, 530.00, NULL, 330.00),
(1, 1450.00, '2024-10-01', 2, 1600.00, 10.0, 1350.00),
(2, 700.00, '2024-10-05', 4, 870.00, 20.0, 670.00),
(3, 280.00, '2024-10-10', 3, 330.00, 15.0, 240.00),
(4, 540.00, '2024-10-15', NULL, 540.00, NULL, 440.00),
(5, 800.00, '2024-10-20', NULL, 800.00, NULL, 650.00),
(6, 540.00, '2024-10-25', NULL, 540.00, NULL, 440.00),
(7, 450.00, '2024-11-01', 3, 530.00, 15.0, 420.00),
(8, 330.00, '2024-11-05', 8, 400.00, 40.0, 270.00),
(9, 500.00, '2024-11-10', 1, 530.00, 5.0, 220.00),
(10, 550.00, '2024-11-15', NULL, 550.00, NULL, 350.00),
(1, 1500.00, '2024-12-01', 2, 1650.00, 10.0, 1400.00),
(2, 720.00, '2024-12-05', 4, 890.00, 20.0, 690.00),
(3, 290.00, '2024-12-10', 3, 340.00, 15.0, 250.00),
(4, 560.00, '2024-12-15', NULL, 560.00, NULL, 460.00),
(5, 820.00, '2024-12-20', NULL, 820.00, NULL, 670.00),
(6, 560.00, '2024-12-25', NULL, 560.00, NULL, 450.00),
(7, 460.00, '2024-12-30', 3, 540.00, 15.0, 430.00),
(8, 340.00, '2025-01-01', 8, 540.00, 40.0, 280.00),
(9, 510.00, '2025-01-05', 1, 540.00, 5.0, 230.00),
(10, 570.00, '2025-01-10', NULL, 570.00, NULL, 360.00);


INSERT INTO Suppliers (SuppliersName) 
VALUES 
('TechSupplier Inc.'),
('GadgetWorld Ltd.'),
('OfficeSupplies Co.');

INSERT INTO Supplier_Email (Suppliers_ID, SuppliersEmail) 
VALUES 
(1, 'contact@techsupplier.com'),
(2, 'info@gadgetworld.com'),
(3, 'support@officesupplies.com');

INSERT INTO Supplier_Number (Suppliers_ID, SupplierNumber) 
VALUES 
(1, 1122334455),
(2, 5566778899),
(3, 9988776655);

INSERT INTO Employee (EmployeeName, Password, Salary, Status) 
VALUES 
('walaa Jaradat', 'walaa1234>', 3000.00, 'manager'),
('Tuleen Rimawi', 'tuleen3.3Rimawi', 3000.00, 'manager'),
('Abdallah Hosheah', 'abdallah1234>', 3000.00, 'manager');

INSERT INTO Employee_Email (Employee_Id, EmployeeEmail) 
VALUES 
(1, 'WalaaJaradat@gmail.com'),
(2, 'TuleenRimawi@gmail.com'),
(3, 'AbdallahHosheah@gmail.com');

INSERT INTO Employee_Number (Employee_Id, EmployeeNumber) 
VALUES 
(1, 1231231234),
(2, 4564564567),
(3, 7897897890);

INSERT INTO Employee (EmployeeName, Password, Salary, Status)
VALUES 
('Ali Jaber', 'ali@secure2024', 2400.00, 'employee'),
('Sara Mahmoud', 'sara.pass2025', 2500.00, 'employee'),
('Hassan Ali', 'hassan1234$', 2200.00, 'employee'),
('Lina Ahmad', 'lina!ahmad2024', 2300.00, 'employee'),
('Omar Khalid', 'omar@khalid2025', 2500.00, 'employee'),
('Nour Jamal', 'nour@securepass1', 2400.00, 'employee'),
('Rami Yousef', 'rami.y2025$', 2600.00, 'employee'),
('Hiba Saeed', 'hiba@2024safe', 2450.00, 'employee'),
('Samir Zaid', 'samir@zaid123', 2350.00, 'employee'),
('Nada Bassam', 'nada.bassam2025', 2550.00, 'employee');
INSERT INTO Employee_Email (Employee_Id, EmployeeEmail)
VALUES
(4, 'AliJaber@gmail.com'),
(5, 'SaraMahmoud@gmail.com'),
(6, 'HassanAli@gmail.com'),
(7, 'LinaAhmad@gmail.com'),
(8, 'OmarKhalid@gmail.com'),
(9, 'NourJamal@gmail.com'),
(10, 'RamiYousef@gmail.com'),
(11, 'HibaSaeed@gmail.com'),
(12, 'SamirZaid@gmail.com'),
(13, 'NadaBassam@gmail.com');
INSERT INTO Employee_Number (Employee_Id, EmployeeNumber)
VALUES
(4, 1231231234),
(5, 9879879870),
(6, 1122334455),
(7, 2233445566),
(8, 3344556677),
(9, 4455667788),
(10, 5566778899),
(11, 6677889900),
(12, 7788990011),
(13, 8899001122);







