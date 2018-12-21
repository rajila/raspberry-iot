using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel.DataAnnotations;

namespace dispensatorService.Models
{
    public class FoodHour
    {
        [Key]
        public int FoodHourId { get; set; }
        public string FoodHourType { get; set; }

        [DataType(DataType.Time)]
        public DateTime Hour { get; set; }
    }
}