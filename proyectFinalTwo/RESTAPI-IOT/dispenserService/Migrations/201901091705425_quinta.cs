namespace dispenserService.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class quinta : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Configurations", "AmoundBowlFoodWater", c => c.Double(nullable: false));
            DropColumn("dbo.Configurations", "AmoundPortionFood");
            DropColumn("dbo.Configurations", "MinPercentWater");
        }
        
        public override void Down()
        {
            AddColumn("dbo.Configurations", "MinPercentWater", c => c.Int(nullable: false));
            AddColumn("dbo.Configurations", "AmoundPortionFood", c => c.Double(nullable: false));
            DropColumn("dbo.Configurations", "AmoundBowlFoodWater");
        }
    }
}
