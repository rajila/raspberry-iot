namespace dispenserService.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class tercera : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.FoodDispensers", "CurrentDateTime", c => c.DateTime(nullable: false));
            AddColumn("dbo.Logs", "CurrentDateTime", c => c.DateTime(nullable: false));
            AddColumn("dbo.WaterDispensers", "CurrentDateTime", c => c.DateTime(nullable: false));
            DropColumn("dbo.FoodDispensers", "CurrentDate");
            DropColumn("dbo.FoodDispensers", "CurrentTime");
            DropColumn("dbo.Logs", "CurrentDate");
            DropColumn("dbo.Logs", "CurrentTime");
            DropColumn("dbo.WaterDispensers", "CurrentDate");
            DropColumn("dbo.WaterDispensers", "CurrentTime");
        }
        
        public override void Down()
        {
            AddColumn("dbo.WaterDispensers", "CurrentTime", c => c.DateTime(nullable: false));
            AddColumn("dbo.WaterDispensers", "CurrentDate", c => c.DateTime(nullable: false));
            AddColumn("dbo.Logs", "CurrentTime", c => c.DateTime(nullable: false));
            AddColumn("dbo.Logs", "CurrentDate", c => c.DateTime(nullable: false));
            AddColumn("dbo.FoodDispensers", "CurrentTime", c => c.DateTime(nullable: false));
            AddColumn("dbo.FoodDispensers", "CurrentDate", c => c.DateTime(nullable: false));
            DropColumn("dbo.WaterDispensers", "CurrentDateTime");
            DropColumn("dbo.Logs", "CurrentDateTime");
            DropColumn("dbo.FoodDispensers", "CurrentDateTime");
        }
    }
}
