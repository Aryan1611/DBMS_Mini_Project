import java.sql.*;
import java.util.*;

public class DBMS_Mini_Project {

	public static void main(String[] args) throws Exception
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		PreparedStatement pstmt = null;
		
		try 
		{
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen", "root" , "");
	}
	
	catch(Exception e)
	{
		System.out.println(e);
	}
	myStmt = myConn.createStatement();
	boolean mainflag = true;
	while (mainflag==true)
	{
		
		System.out.println("\n\nWelcome to Canteen Management System !\n\n\n1.Admin Login\n2.Register User\n3.User Login\n4.Exit");
		int opt = scanint(0);
		switch(opt)
		{
		case 1:
			adminlogin(myConn,myStmt,myRs,pstmt);
			break;
			
		case 2:
			registeruser(myConn,myStmt,myRs,pstmt);
			break;
			
		case 3:
			userlogin(myConn,myStmt,myRs,pstmt);
			break;
			
		case 4:
			System.out.println("Bye !! :)");
			return;
		default:
			System.out.println("Please select correct option !!");
			}
		}
	}

public static void adminlogin(Connection myConn,Statement myStmt,ResultSet myRs,PreparedStatement pstmt) throws SQLException
{
	try 
	{
		myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen", "root" , "");
	}
	
	catch(Exception e)
	{
		System.out.println(e);
	}
	myStmt = myConn.createStatement();
	String query = "";
	System.out.println("Enter Username :");
	String uname = scanstring("");
	System.out.println("Enter Password :");
	String pass = scanstring("");
	query = "select * from admin";
	boolean checkflag = false;
	myRs = myStmt.executeQuery(query);
	while (myRs.next()) 
	{
		String username = myRs.getString("admin_name");
		String password = myRs.getString("admin_password");
		if(username.equals(uname) && password.equals(pass))
		{
			boolean adminflag = true;
			while (adminflag==true) 
			{
				System.out.println("\n1.Insert Food Item\n2.Update Food Quantity\n3.Update Food Price\n4.Delete Food Item\n5.View Items\n6.View users\n7.Exit");
				
				int choice = scanint(0);
				switch(choice)
				{
				case 1:
					String name;
					try 
					{
						System.out.println("Enter the name : ");
						name = scanstring("");
						
						System.out.println("Enter the Price : ");
						int price = scanint(0);
						
						System.out.println("Enter the quantity : ");
						int quantity = scanint(0);
						
						query = "insert into food values (NULL,?,?,?)";
						pstmt = myConn.prepareStatement(query);
						pstmt.setString(1, name);
						pstmt.setInt(2, price);
						pstmt.setInt(3, quantity);
						pstmt.executeUpdate();
						System.out.println("Record successfully inserted !!\n\n");
					} 
					catch (Exception e) 
					{
						System.out.println("Record insertion failed !!");
					}
					break;
					
				case 2:
					System.out.println("Enter the Food Item whose quantity is to be Updated : ");
					name = scanstring("");
					System.out.println("Enter the quantity to be added : ");
					int addquan = scanint(0);
					query = "select * from food where food_name = ?";
					pstmt = myConn.prepareStatement(query);
					pstmt.setString(1, name);
					myRs = pstmt.executeQuery();
					myRs.next();
					int ogquan = myRs.getInt("availability");
					int totquan = ogquan+addquan;
					query = "update food set availability = ? where food_name = ?";
					pstmt = myConn.prepareStatement(query);
					pstmt.setInt(1,totquan);
					pstmt.setString(2,name);
					pstmt.executeUpdate();
					System.out.println("Data successfully updated !!\n\n");
					break;
					
				case 3:
					System.out.println("Enter the Food Item whose price is to be Updated : ");
					name = scanstring("");
					System.out.println("Enter the amount to increase : ");
					int addprice = scanint(0);
					query = "select * from food where food_name = ?";
					pstmt = myConn.prepareStatement(query);
					pstmt.setString(1, name);
					myRs = pstmt.executeQuery();
					myRs.next();
					int ogprice = myRs.getInt("food_price");
					int totprice = ogprice+addprice;
					query = "update food set food_price = ? where food_name = ?";
					pstmt = myConn.prepareStatement(query);
					pstmt.setInt(1,totprice);
					pstmt.setString(2,name);
					pstmt.executeUpdate();
					System.out.println("Data successfully updated !!\n\n");
					break;
					
				case 4:
					System.out.println("Enter the Food Item to be Deleted :");
					name = scanstring("");
					query = "delete from food where food_name=?";
					pstmt = myConn.prepareStatement(query);
					pstmt.setString(1, name);
					pstmt.executeUpdate();
					System.out.println("Record successfully deleted !!\n\n");
					break;
					
				case 5:
					query = "select * from food";
					pstmt = myConn.prepareStatement(query);
					myRs = pstmt.executeQuery();
					System.out.println("Item                Price\tQuantity ");
					while (myRs.next()) 
					{
						System.out.println(myRs.getString("food_name") + padding(myRs.getString("food_name")) + myRs.getInt("food_price") + "\t\t" + myRs.getInt("availability") + "\n\n");
					}
					System.out.println("\n\n");
					break;
					
				case 6:
					viewusers(myConn,myStmt,myRs,pstmt);
					break;
					
				case 7:
					checkflag=true;
			        adminflag=false;
			        return;

				}
			}
		}
		if(checkflag == false)
		{
			System.out.println("Please enter correct details!");
			System.out.println("Are you a verified admin ??(y/n)");
			String reg = scanstring("");
			if(reg.equals("Y") || reg.equals("y"))
			{
				System.out.println("Please enter correct username and password!!\n\n");
				adminlogin(myConn,myStmt,myRs,pstmt);
			}
		}
	}
	
}

private static void registeruser(Connection myConn, Statement myStmt, ResultSet myRs, PreparedStatement pstmt) throws SQLException 
{
	try 
	{
		myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen", "root" , "");
	}
	
	catch(Exception e)
	{
		System.out.println(e);
	}
	myStmt = myConn.createStatement();
	String query = "";
	System.out.println("Please enter your details :");
	System.out.println("First Name :");
	String fname = scanstring("");
	System.out.println("Mobile Number :");
	double phone = scandouble(0);
	while(true)
	{
		if(phone<1000000000)
		{
			System.out.println("Please enter valid mobile number !");
			phone = scanint(0);
		}
		else
			break;
	}
	System.out.println("Email :");
	String email = scanstring("");
	System.out.println("Password :");
	String passw = scanstring("");
	query = "insert into users values (NULL,?,?,?,?)";
	pstmt = myConn.prepareStatement(query);
	pstmt.setString(1, fname);
	pstmt.setDouble(2, phone);
	pstmt.setString(3, email);
	pstmt.setString(4, passw);
	pstmt.executeUpdate();
	System.out.println("Record successfully inserted !!\n\n");
}

private static void userlogin(Connection myConn, Statement myStmt, ResultSet myRs, PreparedStatement pstmt) throws SQLException 
{
	try 
	{
		myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen", "root" , "");
	}
	
	catch(Exception e)
	{
		System.out.println(e);
	}
	myStmt = myConn.createStatement();
	String query = "";
	System.out.println("Enter Username :");
	String useruname = scanstring("");
	System.out.println("Enter Password :");
	String userpass = scanstring("");
	query = "select * from users";
	myRs = myStmt.executeQuery(query);
	boolean checkflag = false;
	while (myRs.next()) 
	{
		String dbusername = myRs.getString("user_name");
		String dbpassword = myRs.getString("user_password");
		int userid = myRs.getInt("user_id");
		if(dbusername.equals(useruname) && dbpassword.equals(userpass))
		{
			checkflag = true;
			System.out.println("Welcome " + useruname);
			boolean userflag = true;
			while(userflag==true)
			{
				System.out.println("\n\n1.Display Menu\n2.Order\n3.Logout");
				System.out.println("Enter your choice :");
				int useropt = scanint(0);
				switch(useropt)
				{
				case 1:
					displaymenu(myConn,myStmt,myRs,pstmt);
					break;
				case 2:
					query = "truncate bill";
					pstmt = myConn.prepareStatement(query);
					pstmt.executeUpdate();
					boolean orderflag = true;
					while(orderflag==true)
					{
						try {
							System.out.println("Enter the Item No. of the food item you wish to order");
							int foodno = scanint(0);
							boolean quanflag = true;
							System.out.println("Enter quantity");
							do
							{
								int quan = scanint(0);
								query = "select * from food where food_id = ?";
								pstmt = myConn.prepareStatement(query);
								pstmt.setInt(1, foodno);
								myRs = pstmt.executeQuery();
								myRs.next();
								String foodname = myRs.getString("food_name");
								int dbquan = myRs.getInt("availability");
								int dbrate = myRs.getInt("food_price");
								if(quan<=dbquan)
								{
									query = "update food set availability = ? where food_id = ?";
									pstmt = myConn.prepareStatement(query);
									pstmt.setInt(1, dbquan-quan );
									pstmt.setInt(2,foodno);
									pstmt.executeUpdate();
									query = "insert into orders values (NULL,?,?,?,?)";
									pstmt = myConn.prepareStatement(query);
									pstmt.setInt(1,quan);
									pstmt.setInt(2, dbrate);
									pstmt.setInt(3, userid);
									pstmt.setInt(4, foodno);
									pstmt.executeUpdate();
									query = "insert into bill values (?,?,?)";
									pstmt = myConn.prepareStatement(query);
									pstmt.setString(1,foodname);
									pstmt.setInt(2, quan);
									pstmt.setInt(3, dbrate);
									pstmt.executeUpdate();
									quanflag = false;
									System.out.println("Order successfully placed !!\n\n");
									System.out.println("Do you wish to order another item ?(y/n):");
									String reorder = scanstring("");
									if(reorder.equals("N") || reorder.equals("n"))
									{
										orderflag=false;
									}
								}
								else
								{
									System.out.println("Sorry! We only have "+dbquan+" left!");
									System.out.println("Please Re-enter quantity :");
								}
							}while(quanflag==true);
						} 
						catch (Exception e)
						{
							System.out.println("Please enter correct food item !!\n\n");
							displaymenu(myConn,myStmt,myRs,pstmt);
						}
						}
					System.out.println("\t\tBILL");
					System.out.println("\n\nFood Item            Quantity\tRate\tTotal Price");
					query = "select * from bill";
					myStmt = myConn.createStatement();
					myRs = myStmt.executeQuery(query);
					int tempfinalrate = 0;
					while(myRs.next())
					{
						String tempname = myRs.getString("food_name");
						int tempquan = myRs.getInt("quantity");
						int temprate = myRs.getInt("rate");
						int temptot = tempquan*temprate;
						tempfinalrate+=temptot;
						System.out.println(tempname + padding(tempname) + tempquan + "\t\t" + temprate + "\t" + temptot);
					}
					System.out.println("\n\nGRAND TOTAL :\t\t" + tempfinalrate + "\n\n");
					query = "truncate bill";
					pstmt = myConn.prepareStatement(query);
					pstmt.executeUpdate();
					break;
				case 3:
					userflag = false;
					return;
					}
				}
			}
		}
	if(checkflag == false)
	{
		System.out.println("Invalid Username or Password !!\n\n");
		System.out.println("Have you registered ??(y/n)");
		String reg = scanstring("");
		if(reg.equals("Y") || reg.equals("y"))
		{
			System.out.println("Please enter correct username and password!!\n\n");
			userlogin(myConn,myStmt,myRs,pstmt);
		}
	}
}

private static void displaymenu(Connection myConn, Statement myStmt, ResultSet myRs, PreparedStatement pstmt) throws SQLException 
{
	try 
	{
		myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen", "root" , "");
	}
	
	catch(Exception e)
	{
		System.out.println(e);
	}
	myStmt = myConn.createStatement();
	String query = "";
	query = "select * from food";
	pstmt = myConn.prepareStatement(query);
	myRs = pstmt.executeQuery();
	System.out.println("Item No\tItem                Price");
	while (myRs.next()) 
	{
		System.out.println(myRs.getInt("food_id")+"\t" + myRs.getString("food_name") + padding(myRs.getString("food_name")) + myRs.getInt("food_price"));
	}
	System.out.println("\n\n");
}

private static void viewusers(Connection myConn, Statement myStmt, ResultSet myRs, PreparedStatement pstmt) throws SQLException 
{
	try 
	{
		myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen", "root" , "");
	}
	
	catch(Exception e)
	{
		System.out.println(e);
	}
	myStmt = myConn.createStatement();
	String query = "";
	query = "select * from users";
	myRs = myStmt.executeQuery(query);
	System.out.println("UserId   Username            Mobile No           Email                                   Password");
	while(myRs.next())
	{
		System.out.println(myRs.getString("user_id") + "        " + myRs.getString("user_name") + padding(myRs.getString("user_name")) + myRs.getString("user_mobile") + "          " + myRs.getString("user_email") + paddingd(myRs.getString("user_email")) + myRs.getString("user_password"));
	}
	System.out.println("\n\n");
}	

private static String padding(String nonpadded)
{
	int length = nonpadded.length();
	String padded = "";
	for(int i=20-length;i>0;i--)
	{
		padded = padded + " ";
	}
	return padded;
}

private static String paddingd(String nonpadded)
{
	int length = nonpadded.length();
	String padded = "";
	for(int i=40-length;i>0;i--)
	{
		padded = padded + " ";
	}
	return padded;
}

private static int scanint(int temp)
{
	Scanner sc = new Scanner(System.in);
		try 
		{
			temp = sc.nextInt();
		} 
		catch (Exception e) 
		{
			System.out.println("Please enter valid input !!!");
			temp = scanint(temp);
		}
		return temp;
}

private static String scanstring(String temp)
{
	Scanner sc = new Scanner(System.in);
	try 
	{
		temp = sc.nextLine();
	} 
	catch (Exception e) 
	{
		System.out.println("Please enter valid input !!!");
			temp = scanstring(temp);
		}
		return temp;
	}

private static double scandouble(double temp)
{
	Scanner sc = new Scanner(System.in);
		try 
		{
			temp = sc.nextDouble();
		} 
		catch (Exception e) 
		{
			System.out.println("Please enter valid input !!!");
			temp = scandouble(temp);
		}
		return temp;
}

}